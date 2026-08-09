package com.vertoedu.service;

import com.vertoedu.repository.AIReviewRepository;
import com.vertoedu.repository.DocumentUploadRepository;
import com.vertoedu.repository.ParentRepository;
import com.vertoedu.repository.StudentRepository;
import com.vertoedu.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AdminDashboardService {

    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final ParentRepository parentRepository;
    private final DocumentUploadRepository documentUploadRepository;
    private final AIReviewRepository aiReviewRepository;

    public Map<String, Object> getDashboardSummary() {
        Map<String, Object> summary = new HashMap<>();
        
        summary.put("totalStudents", studentRepository.count());
        summary.put("totalTeachers", teacherRepository.count());
        summary.put("totalParents", parentRepository.count());
        summary.put("pendingOcr", documentUploadRepository.countByStatus(com.vertoedu.entity.enums.DocumentStatus.UPLOADED));
        summary.put("pendingAiReviews", documentUploadRepository.countByStatus(com.vertoedu.entity.enums.DocumentStatus.OCR_COMPLETED) + documentUploadRepository.countByStatus(com.vertoedu.entity.enums.DocumentStatus.AI_COMPLETED));
        
        java.util.List<Map<String, String>> recentAdmissions = studentRepository.findTop5ByOrderByCreatedAtDesc()
            .stream()
            .map(s -> {
                Map<String, String> map = new HashMap<>();
                map.put("name", s.getFirstName() + " " + s.getLastName());
                map.put("date", s.getCreatedAt() != null ? s.getCreatedAt().toLocalDate().toString() : java.time.LocalDate.now().toString());
                return map;
            })
            .toList();
            
        summary.put("recentAdmissions", recentAdmissions);
        
        return summary;
    }
}
