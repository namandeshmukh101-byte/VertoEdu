package com.vertoedu.service;

import com.vertoedu.entity.DocumentUpload;
import com.vertoedu.entity.User;
import com.vertoedu.entity.enums.DocumentType;
import com.vertoedu.exception.ResourceNotFoundException;
import com.vertoedu.repository.DocumentUploadRepository;
import com.vertoedu.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.apache.tika.Tika;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class DocumentStorageService {

    private final DocumentUploadRepository documentUploadRepository;
    private final UserRepository userRepository;

    private static final String UPLOAD_DIR = "uploads/ocr/";

    @Transactional
    public DocumentUpload uploadDocument(MultipartFile file, DocumentType type, String userEmail) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Cannot upload empty file.");
        }

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userEmail));

        // Validate Extension First
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            throw new IllegalArgumentException("File name is missing.");
        }
        String lowerCaseName = originalFilename.toLowerCase();
        if (!lowerCaseName.endsWith(".pdf") && !lowerCaseName.endsWith(".jpg") 
            && !lowerCaseName.endsWith(".jpeg") && !lowerCaseName.endsWith(".png")) {
            throw new IllegalArgumentException("Only PDF, JPG/JPEG, and PNG documents are accepted.");
        }

        try {
            // Validate Magic Bytes using Apache Tika
            Tika tika = new Tika();
            String detectedType = tika.detect(file.getInputStream());
            if (!detectedType.equals("application/pdf") && 
                !detectedType.equals("image/jpeg") && 
                !detectedType.equals("image/png")) {
                throw new IllegalArgumentException("Invalid file content. Only true PDF, JPG/JPEG, and PNG documents are accepted.");
            }

            // Create directories if they don't exist
            File directory = new File(UPLOAD_DIR);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // Generate unique file name
            String extension = originalFilename != null && originalFilename.contains(".")
                    ? originalFilename.substring(originalFilename.lastIndexOf("."))
                    : "";
            String uniqueFileName = UUID.randomUUID().toString() + extension;
            Path filePath = Paths.get(UPLOAD_DIR + uniqueFileName);

            // Save file locally
            Files.write(filePath, file.getBytes());

            // Save metadata to database
            DocumentUpload documentUpload = new DocumentUpload();
            documentUpload.setFileName(originalFilename);
            documentUpload.setFilePath(filePath.toString());
            documentUpload.setDocumentType(type);
            documentUpload.setUploadedBy(user);

            return documentUploadRepository.save(documentUpload);
        } catch (IOException e) {
            log.error("Failed to store file", e);
            throw new RuntimeException("Failed to store file: " + e.getMessage());
        }
    }

    public List<DocumentUpload> getAllUploads() {
        return documentUploadRepository.findAll();
    }

    public DocumentUpload getDocumentById(Long id) {
        return documentUploadRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Document not found with ID: " + id));
    }
}
