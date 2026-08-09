package com.vertoedu.service;

import com.vertoedu.entity.Parent;
import com.vertoedu.entity.ParentStudentLinkRequest;
import com.vertoedu.entity.Student;
import com.vertoedu.entity.enums.LinkRequestStatus;
import com.vertoedu.exception.ResourceNotFoundException;
import com.vertoedu.repository.ParentRepository;
import com.vertoedu.repository.ParentStudentLinkRequestRepository;
import com.vertoedu.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ParentStudentLinkService {

    private final ParentStudentLinkRequestRepository requestRepository;
    private final ParentRepository parentRepository;
    private final StudentRepository studentRepository;

    @Transactional
    public ParentStudentLinkRequest createRequest(String userEmail, String scholarNumber) {
        Parent parent = parentRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Parent not found for email: " + userEmail));

        // Check if student exists
        studentRepository.findByScholarNumber(scholarNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with Scholar Number: " + scholarNumber));

        // Check if request already exists
        Optional<ParentStudentLinkRequest> existingRequest = requestRepository.findByParentIdAndScholarNumber(parent.getId(), scholarNumber);
        if (existingRequest.isPresent()) {
            ParentStudentLinkRequest req = existingRequest.get();
            if (req.getStatus() == LinkRequestStatus.PENDING) {
                throw new IllegalStateException("A link request is already pending for this scholar number.");
            }
            if (req.getStatus() == LinkRequestStatus.APPROVED) {
                throw new IllegalStateException("You are already linked to this student.");
            }
            // If rejected, we allow them to request again by updating the existing request
            req.setStatus(LinkRequestStatus.PENDING);
            return requestRepository.save(req);
        }

        ParentStudentLinkRequest newRequest = ParentStudentLinkRequest.builder()
                .parent(parent)
                .scholarNumber(scholarNumber)
                .status(LinkRequestStatus.PENDING)
                .build();

        return requestRepository.save(newRequest);
    }

    public List<ParentStudentLinkRequest> getPendingRequests() {
        return requestRepository.findByStatus(LinkRequestStatus.PENDING);
    }

    public List<ParentStudentLinkRequest> getMyRequests(String userEmail) {
        Parent parent = parentRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Parent not found for email: " + userEmail));
        return requestRepository.findByParentId(parent.getId());
    }

    @Transactional
    public void approveRequest(Long requestId) {
        ParentStudentLinkRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Request not found"));
        
        if (request.getStatus() != LinkRequestStatus.PENDING) {
            throw new IllegalStateException("Request is not in PENDING state");
        }

        Student student = studentRepository.findByScholarNumber(request.getScholarNumber())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found for Scholar Number: " + request.getScholarNumber()));

        // Perform the link
        student.setParent(request.getParent());
        studentRepository.save(student);

        request.setStatus(LinkRequestStatus.APPROVED);
        requestRepository.save(request);
        log.info("Approved link request {} for parent {} and student {}", requestId, request.getParent().getId(), student.getScholarNumber());
    }

    @Transactional
    public void rejectRequest(Long requestId) {
        ParentStudentLinkRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Request not found"));
        
        if (request.getStatus() != LinkRequestStatus.PENDING) {
            throw new IllegalStateException("Request is not in PENDING state");
        }

        request.setStatus(LinkRequestStatus.REJECTED);
        requestRepository.save(request);
        log.info("Rejected link request {}", requestId);
    }

    @Transactional
    public void removeLink(Long parentId, String scholarNumber) {
        Student student = studentRepository.findByScholarNumber(scholarNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

        if (student.getParent() != null && student.getParent().getId().equals(parentId)) {
            student.setParent(null);
            studentRepository.save(student);
        }
        
        Optional<ParentStudentLinkRequest> existingRequest = requestRepository.findByParentIdAndScholarNumber(parentId, scholarNumber);
        existingRequest.ifPresent(requestRepository::delete);
        
        log.info("Removed link between parent {} and student {}", parentId, scholarNumber);
    }
}
