package com.vertoedu.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.Map;

@Data
public class ApprovalRequest {
    @NotNull
    private Long documentUploadId;
    
    @NotNull
    private Boolean isApproved;
    
    // In case the admin modifies the JSON manually before saving
    private Map<String, Object> finalApprovedData;
    
    private String rejectionReason;
}
