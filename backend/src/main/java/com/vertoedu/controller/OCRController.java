package com.vertoedu.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vertoedu.dto.DocumentUploadResponse;
import com.vertoedu.dto.OCRResultResponse;
import com.vertoedu.entity.DocumentUpload;
import com.vertoedu.entity.OCRResult;
import com.vertoedu.entity.enums.DocumentType;
import com.vertoedu.service.DocumentStorageService;
import com.vertoedu.service.OCRService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/ocr")
@RequiredArgsConstructor
public class OCRController {

    private final DocumentStorageService storageService;
    private final OCRService ocrService;
    private final ObjectMapper objectMapper;

    @PostMapping("/upload")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DocumentUploadResponse> uploadDocument(
            @RequestParam("file") MultipartFile file,
            @RequestParam("type") String type,
            Authentication authentication) {

        DocumentType docType = DocumentType.valueOf(type.toUpperCase());
        DocumentUpload upload = storageService.uploadDocument(file, docType, authentication.getName());
        
        return ResponseEntity.ok(mapToResponse(upload));
    }

    @PostMapping("/process/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OCRResultResponse> processOCR(@PathVariable Long id) {
        DocumentUpload document = storageService.getDocumentById(id);
        OCRResult result = ocrService.processOCR(document);
        return ResponseEntity.ok(mapToOCRResponse(result));
    }
    
    @GetMapping("/result/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OCRResultResponse> getOCRResult(@PathVariable Long id) {
        OCRResult result = ocrService.getOCRResultForDocument(id);
        if (result == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(mapToOCRResponse(result));
    }

    @GetMapping("/history")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<DocumentUploadResponse>> getUploadHistory() {
        List<DocumentUploadResponse> history = storageService.getAllUploads().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(history);
    }

    private DocumentUploadResponse mapToResponse(DocumentUpload doc) {
        return DocumentUploadResponse.builder()
                .id(doc.getId())
                .fileName(doc.getFileName())
                .documentType(doc.getDocumentType().name())
                .status(doc.getStatus().name())
                .uploadedBy(doc.getUploadedBy().getEmail())
                .uploadedAt(doc.getCreatedAt())
                .build();
    }
    
    private OCRResultResponse mapToOCRResponse(OCRResult result) {
        Object extractedData = null;
        try {
            extractedData = objectMapper.readValue(result.getExtractedDataJson(), Map.class);
        } catch (JsonProcessingException e) {
            extractedData = result.getExtractedDataJson();
        }
        return OCRResultResponse.builder()
                .id(result.getId())
                .documentUploadId(result.getDocumentUpload().getId())
                .rawText(result.getRawText())
                .extractedData(extractedData)
                .build();
    }
}
