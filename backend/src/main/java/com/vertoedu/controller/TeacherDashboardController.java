package com.vertoedu.controller;

import com.vertoedu.dto.ApiResponse;
import com.vertoedu.dto.SectionDto;
import com.vertoedu.dto.StudentDto;
import com.vertoedu.dto.SubjectDto;
import com.vertoedu.security.JwtTokenProvider;
import com.vertoedu.service.TeacherDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/teacher")
@PreAuthorize("hasRole('TEACHER')")
@RequiredArgsConstructor
public class TeacherDashboardController {

    private final TeacherDashboardService dashboardService;
    private final JwtTokenProvider jwtTokenProvider;

    private final com.vertoedu.repository.UserRepository userRepository;

    @GetMapping("/summary")
    public ResponseEntity<ApiResponse<java.util.Map<String, Object>>> getDashboardSummary(Authentication authentication) {
        Long userId = getUserId(authentication);
        return ResponseEntity.ok(ApiResponse.success("Dashboard summary retrieved successfully",
                dashboardService.getDashboardSummary(userId)));
    }

    @GetMapping("/me/classes")
    public ResponseEntity<ApiResponse<List<SectionDto>>> getAssignedClasses(Authentication authentication) {
        Long userId = getUserId(authentication);
        return ResponseEntity.ok(ApiResponse.success("Assigned classes fetched", dashboardService.getAssignedSections(userId)));
    }

    @GetMapping("/me/classes/{sectionId}/students")
    public ResponseEntity<ApiResponse<List<StudentDto>>> getStudents(Authentication authentication, @PathVariable Long sectionId) {
        Long userId = getUserId(authentication);
        return ResponseEntity.ok(ApiResponse.success("Students fetched", dashboardService.getStudentsForSection(userId, sectionId)));
    }

    @GetMapping("/me/subjects")
    public ResponseEntity<ApiResponse<List<SubjectDto>>> getAssignedSubjects(Authentication authentication) {
        Long userId = getUserId(authentication);
        return ResponseEntity.ok(ApiResponse.success("Assigned subjects fetched", dashboardService.getAssignedSubjects(userId)));
    }

    private Long getUserId(Authentication authentication) {
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new com.vertoedu.exception.ResourceNotFoundException("User not found"))
                .getId();
    }
}
