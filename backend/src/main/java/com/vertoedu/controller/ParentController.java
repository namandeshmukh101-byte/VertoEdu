package com.vertoedu.controller;

import com.vertoedu.dto.ApiResponse;
import com.vertoedu.dto.AttendanceRecordDto;
import com.vertoedu.dto.ExamResultDto;
import com.vertoedu.dto.ParentDto;
import com.vertoedu.dto.ParentProfileUpdateDto;
import com.vertoedu.dto.StudentProfileDto;
import com.vertoedu.service.AttendanceQueryService;
import com.vertoedu.service.ParentService;
import com.vertoedu.service.ResultQueryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/parent")
@RequiredArgsConstructor
@PreAuthorize("hasRole('PARENT')")
public class ParentController {

    private final ParentService parentService;
    private final AttendanceQueryService attendanceQueryService;
    private final ResultQueryService resultQueryService;
    private final com.vertoedu.repository.ExamRepository examRepository;

    @GetMapping("/exams")
    public ResponseEntity<ApiResponse<List<com.vertoedu.dto.ExamDto>>> getUpcomingExams() {
        // Fetch exams. Hardcoding academicYearId=1 for prototype purposes.
        List<com.vertoedu.dto.ExamDto> exams = examRepository.findByAcademicYearId(1L).stream()
            .map(e -> {
                com.vertoedu.dto.ExamDto dto = new com.vertoedu.dto.ExamDto();
                dto.setId(e.getId());
                dto.setName(e.getName());
                dto.setAcademicYearId(e.getAcademicYear().getId());
                return dto;
            }).toList();
        return ResponseEntity.ok(ApiResponse.success("Upcoming exams retrieved", exams));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<ParentDto>> getParentProfile(Authentication authentication) {
        String email = authentication.getName();
        ParentDto profile = parentService.getParentProfile(email);
        return ResponseEntity.ok(ApiResponse.success("Parent profile retrieved successfully", profile));
    }

    @PutMapping("/me")
    public ResponseEntity<ApiResponse<ParentDto>> updateParentProfile(
            Authentication authentication,
            @Valid @RequestBody ParentProfileUpdateDto updateDto) {
        String email = authentication.getName();
        ParentDto updatedProfile = parentService.updateParentProfile(email, updateDto);
        return ResponseEntity.ok(ApiResponse.success("Parent profile updated successfully", updatedProfile));
    }

    @GetMapping("/me/students")
    public ResponseEntity<ApiResponse<List<StudentProfileDto>>> getLinkedStudents(Authentication authentication) {
        String email = authentication.getName();
        List<StudentProfileDto> students = parentService.getLinkedStudents(email);
        return ResponseEntity.ok(ApiResponse.success("Linked students retrieved successfully", students));
    }

    @GetMapping("/students/{studentId}/attendance")
    public ResponseEntity<ApiResponse<List<AttendanceRecordDto>>> getStudentAttendance(
            Authentication authentication,
            @PathVariable Long studentId) {
        
        String email = authentication.getName();
        verifyStudentOwnership(email, studentId);
        
        List<AttendanceRecordDto> records = attendanceQueryService.getStudentAttendanceHistory(studentId);
        return ResponseEntity.ok(ApiResponse.success("Student attendance retrieved successfully", records));
    }

    @GetMapping("/students/{studentId}/results")
    public ResponseEntity<ApiResponse<List<ExamResultDto>>> getStudentResults(
            Authentication authentication,
            @PathVariable Long studentId) {
        
        String email = authentication.getName();
        verifyStudentOwnership(email, studentId);
        
        List<ExamResultDto> results = resultQueryService.getStudentExamResults(studentId);
        return ResponseEntity.ok(ApiResponse.success("Student results retrieved successfully", results));
    }

    private void verifyStudentOwnership(String email, Long studentId) {
        List<StudentProfileDto> linkedStudents = parentService.getLinkedStudents(email);
        boolean ownsStudent = linkedStudents.stream().anyMatch(s -> s.getId().equals(studentId));
        if (!ownsStudent) {
            throw new org.springframework.security.access.AccessDeniedException("You are not authorized to view data for this student");
        }
    }
}
