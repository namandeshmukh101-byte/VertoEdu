package com.vertoedu.controller;

import com.vertoedu.dto.ApprovalRequest;
import com.vertoedu.entity.ApprovalLog;
import com.vertoedu.service.ApprovalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/approval")
@RequiredArgsConstructor
public class ApprovalController {

    private final ApprovalService approvalService;

    @PostMapping("/submit")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> submitApproval(
            @Valid @RequestBody ApprovalRequest request,
            Authentication authentication) {
        
        ApprovalLog log = approvalService.processApproval(request, authentication.getName());
        
        String message = request.getIsApproved() 
                ? "Document approved and data saved successfully." 
                : "Document rejected successfully.";
                
        return ResponseEntity.ok(Map.of("success", "true", "message", message));
    }
}
