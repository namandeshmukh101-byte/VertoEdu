package com.vertoedu.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AIReviewResponse {
    private Long id;
    private Long ocrResultId;
    private String suggestionsText;
    private Object suggestedData;
}
