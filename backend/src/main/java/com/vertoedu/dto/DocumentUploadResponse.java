package com.vertoedu.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class DocumentUploadResponse {
    private Long id;
    private String fileName;
    private String documentType;
    private String status;
    private String uploadedBy;
    private LocalDateTime uploadedAt;
}
