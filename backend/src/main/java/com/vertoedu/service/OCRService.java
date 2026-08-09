package com.vertoedu.service;

import com.vertoedu.entity.DocumentUpload;
import com.vertoedu.entity.OCRResult;
import com.vertoedu.entity.enums.DocumentStatus;
import com.vertoedu.exception.OcrServiceException;
import com.vertoedu.repository.DocumentUploadRepository;
import com.vertoedu.repository.OCRResultRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class OCRService {

    private final OCRResultRepository ocrResultRepository;
    private final DocumentUploadRepository documentUploadRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${ocr.service.url:http://localhost:8000/ocr}")
    private String ocrServiceUrl;

    @Transactional
    public OCRResult processOCR(DocumentUpload documentUpload) {
        log.info("Starting REAL OCR processing for document ID: {}", documentUpload.getId());
        
        // Check if already processed
        ocrResultRepository.findByDocumentUploadId(documentUpload.getId())
                .ifPresent(ocrResultRepository::delete);

        File documentFile = new File(documentUpload.getFilePath());
        if (!documentFile.exists()) {
            throw new RuntimeException("Document file not found at: " + documentUpload.getFilePath());
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", new FileSystemResource(documentFile));

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        String rawText = "";
        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(ocrServiceUrl, requestEntity, Map.class);
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                Boolean success = (Boolean) response.getBody().get("success");
                if (Boolean.TRUE.equals(success)) {
                    rawText = (String) response.getBody().get("rawText");
                } else {
                    throw new RuntimeException("OCR Service returned success=false");
                }
            } else {
                throw new OcrServiceException("OCR Service returned status: " + response.getStatusCode());
            }
        } catch (Exception e) {
            log.error("Failed to communicate with OCR service", e);
            documentUpload.setStatus(DocumentStatus.REJECTED);
            documentUploadRepository.save(documentUpload);
            throw new OcrServiceException("Real OCR Service failed: " + e.getMessage());
        }

        if (rawText == null || rawText.trim().isEmpty()) {
            documentUpload.setStatus(DocumentStatus.REJECTED);
            documentUploadRepository.save(documentUpload);
            throw new OcrServiceException("OCR Service returned empty text. OCR Failed.");
        }

        OCRResult ocrResult = new OCRResult();
        ocrResult.setDocumentUpload(documentUpload);
        ocrResult.setRawText(rawText);
        // Extracted JSON is no longer populated by OCR (AI does that now). Just store empty {}
        ocrResult.setExtractedDataJson("{}");

        OCRResult savedResult = ocrResultRepository.save(ocrResult);

        // Update Document Status
        documentUpload.setStatus(DocumentStatus.OCR_COMPLETED);
        documentUploadRepository.save(documentUpload);

        log.info("REAL OCR completed successfully for document ID: {}", documentUpload.getId());
        return savedResult;
    }
    
    public OCRResult getOCRResultForDocument(Long documentId) {
        return ocrResultRepository.findByDocumentUploadId(documentId)
                .orElse(null);
    }
}
