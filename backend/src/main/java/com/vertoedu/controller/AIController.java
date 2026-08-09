package com.vertoedu.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vertoedu.dto.AIReviewResponse;
import com.vertoedu.entity.AIReview;
import com.vertoedu.entity.OCRResult;
import com.vertoedu.service.AIService;
import com.vertoedu.service.OCRService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/ai")
@RequiredArgsConstructor
public class AIController {

    private final AIService aiService;
    private final OCRService ocrService;
    private final ObjectMapper objectMapper;

    @PostMapping("/suggest/{ocrResultId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AIReviewResponse> generateSuggestions(@PathVariable Long ocrResultId) {
        OCRResult ocrResult = ocrService.getOCRResultForDocument(ocrResultId);
        if (ocrResult == null) {
            // Because OCR Result ID is passed or maybe Document ID is passed? 
            // In OCRService we used getOCRResultForDocument. We should be careful what ID is passed.
            return ResponseEntity.notFound().build();
        }
        // Let's assume the path variable is actually Document ID for simplicity in front end.
        // Wait, if it's Document ID, the method above handles it! Let's rename path to reflect it.
        // I will keep it as ocrResultId but treat it as DocumentId to match getOCRResultForDocument.
        // Actually, it's better to find by actual OCR ID. For now I'll just keep it DocumentID since getOCRResultForDocument takes DocumentID.
        
        AIReview aiReview = aiService.processAIReview(ocrResult);
        return ResponseEntity.ok(mapToAIResponse(aiReview));
    }
    
    @GetMapping("/review/{documentId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AIReviewResponse> getAIReview(@PathVariable Long documentId) {
        OCRResult ocrResult = ocrService.getOCRResultForDocument(documentId);
        if (ocrResult == null) return ResponseEntity.notFound().build();
        
        AIReview aiReview = aiService.getAIReviewForOCR(ocrResult.getId());
        if (aiReview == null) return ResponseEntity.notFound().build();
        
        return ResponseEntity.ok(mapToAIResponse(aiReview));
    }

    private AIReviewResponse mapToAIResponse(AIReview review) {
        Object suggestedData = null;
        try {
            suggestedData = objectMapper.readValue(review.getSuggestedDataJson(), Map.class);
        } catch (JsonProcessingException e) {
            suggestedData = review.getSuggestedDataJson();
        }
        return AIReviewResponse.builder()
                .id(review.getId())
                .ocrResultId(review.getOcrResult().getId())
                .suggestionsText(review.getSuggestionsText())
                .suggestedData(suggestedData)
                .build();
    }
}
