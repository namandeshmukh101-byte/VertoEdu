package com.vertoedu.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vertoedu.dto.ApprovalRequest;
import com.vertoedu.entity.*;
import com.vertoedu.entity.enums.DocumentStatus;
import com.vertoedu.exception.ResourceNotFoundException;
import com.vertoedu.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApprovalService {

    private final ApprovalLogRepository approvalLogRepository;
    private final DocumentUploadRepository documentUploadRepository;
    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    public ApprovalLog processApproval(ApprovalRequest request, String adminEmail) {
        User admin = userRepository.findByEmail(adminEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Admin not found"));

        DocumentUpload document = documentUploadRepository.findById(request.getDocumentUploadId())
                .orElseThrow(() -> new ResourceNotFoundException("Document not found"));

        ApprovalLog logEntry = new ApprovalLog();
        logEntry.setDocumentUpload(document);
        logEntry.setAdminUser(admin);
        logEntry.setApproved(request.getIsApproved());

        if (request.getIsApproved()) {
            try {
                // Parse the final JSON and insert into main database
                Map<String, Object> data = request.getFinalApprovedData();
                String finalJsonStr = objectMapper.writeValueAsString(data);
                logEntry.setFinalApprovedDataJson(finalJsonStr);

                // Assuming this is an Admission Form, we create a Student.
                Student newStudent = new Student();
                newStudent.setFirstName((String) data.get("firstName"));
                newStudent.setLastName((String) data.get("lastName"));
                newStudent.setScholarNumber("SCH" + UUID.randomUUID().toString().substring(0, 6).toUpperCase());
                
                String dobStr = (String) data.get("dateOfBirth");
                if (dobStr != null && !dobStr.isEmpty()) {
                    newStudent.setDob(LocalDate.parse(dobStr));
                }

                // If school is set on document, use it
                if (document.getSchool() != null) {
                    newStudent.setSchool(document.getSchool());
                }

                // Parent logic omitted for brevity, but would be handled similarly
                studentRepository.save(newStudent);
                
                document.setStatus(DocumentStatus.APPROVED);
                log.info("Student {} {} successfully created via OCR Approval.", newStudent.getFirstName(), newStudent.getLastName());
            } catch (Exception e) {
                log.error("Failed to process approval data into core database", e);
                throw new RuntimeException("Failed to save approved data: " + e.getMessage());
            }
        } else {
            logEntry.setRejectionReason(request.getRejectionReason());
            document.setStatus(DocumentStatus.REJECTED);
            log.info("OCR Document {} rejected by {}", document.getId(), adminEmail);
        }

        documentUploadRepository.save(document);
        return approvalLogRepository.save(logEntry);
    }
}
