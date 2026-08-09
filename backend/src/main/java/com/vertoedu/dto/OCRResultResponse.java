package com.vertoedu.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OCRResultResponse {
    private Long id;
    private Long documentUploadId;
    private String rawText;
    private Object extractedData; // using Object to send JSON natively
}
