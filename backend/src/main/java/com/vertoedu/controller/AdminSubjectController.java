package com.vertoedu.controller;

import com.vertoedu.dto.ApiResponse;
import com.vertoedu.dto.SubjectDto;
import com.vertoedu.service.SubjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/subjects")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminSubjectController {

    private final SubjectService subjectService;

    @GetMapping("/school/{schoolId}")
    public ApiResponse<List<SubjectDto>> getSubjectsBySchool(@PathVariable Long schoolId) {
        return ApiResponse.success("Subjects retrieved successfully", subjectService.getSubjectsBySchool(schoolId));
    }

    @GetMapping("/{id}")
    public ApiResponse<SubjectDto> getSubjectById(@PathVariable Long id) {
        return ApiResponse.success("Subject retrieved successfully", subjectService.getSubjectById(id));
    }

    @PostMapping
    public ApiResponse<SubjectDto> createSubject(@Valid @RequestBody SubjectDto dto) {
        return ApiResponse.success("Subject created successfully", subjectService.createSubject(dto));
    }

    @PutMapping("/{id}")
    public ApiResponse<SubjectDto> updateSubject(@PathVariable Long id, @Valid @RequestBody SubjectDto dto) {
        return ApiResponse.success("Subject updated successfully", subjectService.updateSubject(id, dto));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteSubject(@PathVariable Long id) {
        subjectService.deleteSubject(id);
        return ApiResponse.success("Subject deleted successfully");
    }
}
