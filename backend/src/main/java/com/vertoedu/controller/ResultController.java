package com.vertoedu.controller;

import com.vertoedu.dto.ApiResponse;
import com.vertoedu.dto.ExamDto;
import com.vertoedu.dto.ExamResultDto;
import com.vertoedu.service.ExamService;
import com.vertoedu.service.ResultService;
import jakarta.validation.Valid;
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
public class ResultController {

    private final ResultService resultService;
    private final ExamService examService;
    private final com.vertoedu.repository.UserRepository userRepository;

    @GetMapping("/exams")
    public ResponseEntity<ApiResponse<List<ExamDto>>> getExams(@RequestParam Long academicYearId) {
        return ResponseEntity.ok(ApiResponse.success("Exams fetched", 
                examService.getExamsByAcademicYear(academicYearId)));
    }

    @GetMapping("/results")
    public ResponseEntity<ApiResponse<List<ExamResultDto>>> getResults(
            @RequestParam Long sectionId,
            @RequestParam Long subjectId,
            @RequestParam Long examId) {
        return ResponseEntity.ok(ApiResponse.success("Results fetched", 
                resultService.getResults(sectionId, subjectId, examId)));
    }

    @PostMapping("/results")
    public ResponseEntity<ApiResponse<ExamResultDto>> saveResult(
            @Valid @RequestBody ExamResultDto dto,
            Authentication authentication) {
        Long userId = getUserId(authentication);
        return ResponseEntity.ok(ApiResponse.success("Result saved", 
                resultService.saveResult(dto, userId)));
    }

    @PutMapping("/results/{id}")
    public ResponseEntity<ApiResponse<ExamResultDto>> updateResult(
            @PathVariable Long id,
            @Valid @RequestBody ExamResultDto dto,
            Authentication authentication) {
        Long userId = getUserId(authentication);
        return ResponseEntity.ok(ApiResponse.success("Result updated", 
                resultService.updateResult(id, dto, userId)));
    }

    private Long getUserId(Authentication authentication) {
        return userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new com.vertoedu.exception.ResourceNotFoundException("User not found"))
                .getId();
    }
}
