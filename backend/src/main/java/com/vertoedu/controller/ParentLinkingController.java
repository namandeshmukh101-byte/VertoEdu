package com.vertoedu.controller;

import com.vertoedu.entity.ParentStudentLinkRequest;
import com.vertoedu.service.ParentStudentLinkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/parents/link")
@RequiredArgsConstructor
public class ParentLinkingController {

    private final ParentStudentLinkService linkService;

    @PostMapping("/request")
    @PreAuthorize("hasRole('PARENT')")
    public ResponseEntity<?> requestLink(@RequestBody Map<String, String> payload, Authentication authentication) {
        String scholarNumber = payload.get("scholarNumber");
        if (scholarNumber == null || scholarNumber.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Scholar Number is required");
        }
        
        ParentStudentLinkRequest request = linkService.createRequest(authentication.getName(), scholarNumber.trim());
        return ResponseEntity.ok(request);
    }

    @GetMapping("/my-requests")
    @PreAuthorize("hasRole('PARENT')")
    public ResponseEntity<List<ParentStudentLinkRequest>> getMyRequests(Authentication authentication) {
        return ResponseEntity.ok(linkService.getMyRequests(authentication.getName()));
    }

    @GetMapping("/pending")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ParentStudentLinkRequest>> getPendingRequests() {
        return ResponseEntity.ok(linkService.getPendingRequests());
    }

    @PostMapping("/{id}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> approveRequest(@PathVariable Long id) {
        linkService.approveRequest(id);
        return ResponseEntity.ok(Map.of("message", "Request approved successfully"));
    }

    @PostMapping("/{id}/reject")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> rejectRequest(@PathVariable Long id) {
        linkService.rejectRequest(id);
        return ResponseEntity.ok(Map.of("message", "Request rejected"));
    }

    @DeleteMapping("/{parentId}/student/{scholarNumber}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> removeLink(@PathVariable Long parentId, @PathVariable String scholarNumber) {
        linkService.removeLink(parentId, scholarNumber);
        return ResponseEntity.ok(Map.of("message", "Link removed successfully"));
    }
}
