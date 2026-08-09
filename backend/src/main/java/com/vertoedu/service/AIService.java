package com.vertoedu.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.vertoedu.entity.AIReview;
import com.vertoedu.entity.DocumentUpload;
import com.vertoedu.entity.OCRResult;
import com.vertoedu.entity.enums.DocumentStatus;
import com.vertoedu.repository.AIReviewRepository;
import com.vertoedu.repository.DocumentUploadRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AIService {

    private final AIReviewRepository aiReviewRepository;
    private final DocumentUploadRepository documentUploadRepository;
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${openai.api.key:}")
    private String openAiApiKey;

    @Transactional
    public AIReview processAIReview(OCRResult ocrResult) {
        log.info("Starting REAL AI Review for OCR Result ID: {}", ocrResult.getId());
        
        if (openAiApiKey == null || openAiApiKey.trim().isEmpty()) {
            throw new RuntimeException("AI STATUS = BLOCKED — OPENAI_API_KEY NOT CONFIGURED");
        }

        // Remove existing if reprocessing
        aiReviewRepository.findByOcrResultId(ocrResult.getId())
                .ifPresent(aiReviewRepository::delete);

        String ocrText = ocrResult.getRawText();
        if (ocrText == null || ocrText.trim().isEmpty()) {
            throw new RuntimeException("OCR text is empty, cannot perform AI review.");
        }

        String suggestionsText = "";
        String suggestedJson = "{}";

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(openAiApiKey);

            String systemPrompt = "You are an AI assistant extracting student admission data from OCR text. " +
                                  "Output a JSON object with exactly two keys: " +
                                  "'extractedData' (an object containing string fields: firstName, lastName, dateOfBirth (YYYY-MM-DD format), gradeLevel, address, parentName, contactPhone. If missing, use empty string) " +
                                  "and 'suggestionsText' (a bulleted string of formatting changes made or missing fields).";

            Map<String, Object> requestBody = Map.of(
                    "model", "gpt-4o-mini",
                    "response_format", Map.of("type", "json_object"),
                    "messages", List.of(
                            Map.of("role", "system", "content", systemPrompt),
                            Map.of("role", "user", "content", ocrText)
                    )
            );

            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
            ResponseEntity<String> response = restTemplate.postForEntity("https://api.openai.com/v1/chat/completions", requestEntity, String.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                JsonNode root = objectMapper.readTree(response.getBody());
                JsonNode message = root.path("choices").get(0).path("message").path("content");
                
                JsonNode contentNode = objectMapper.readTree(message.asText());
                
                if (contentNode.has("extractedData")) {
                    suggestedJson = contentNode.get("extractedData").toString();
                }
                if (contentNode.has("suggestionsText")) {
                    suggestionsText = contentNode.get("suggestionsText").asText();
                }
            } else {
                throw new RuntimeException("OpenAI API returned status: " + response.getStatusCode());
            }
        } catch (org.springframework.web.client.HttpClientErrorException e) {
            log.error("HTTP Client error from OpenAI API: {}", e.getStatusCode());
            if (e.getStatusCode().value() == 429) {
                throw new com.vertoedu.exception.AiQuotaExceededException("OpenAI API returned status: 429");
            }
            throw new RuntimeException("Real AI Service failed: " + e.getMessage());
        } catch (Exception e) {
            log.error("Failed to communicate with OpenAI API", e);
            throw new RuntimeException("Real AI Service failed: " + e.getMessage());
        }

        AIReview aiReview = new AIReview();
        aiReview.setOcrResult(ocrResult);
        aiReview.setSuggestionsText(suggestionsText);
        aiReview.setSuggestedDataJson(suggestedJson);

        AIReview savedReview = aiReviewRepository.save(aiReview);

        // Update Document Status
        DocumentUpload document = ocrResult.getDocumentUpload();
        document.setStatus(DocumentStatus.AI_COMPLETED);
        documentUploadRepository.save(document);

        log.info("REAL AI Review completed successfully for OCR Result ID: {}", ocrResult.getId());
        return savedReview;
    }
    
    public AIReview getAIReviewForOCR(Long ocrResultId) {
        return aiReviewRepository.findByOcrResultId(ocrResultId).orElse(null);
    }
}
