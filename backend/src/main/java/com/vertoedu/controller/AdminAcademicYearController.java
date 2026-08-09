package com.vertoedu.controller;

import com.vertoedu.dto.AcademicYearDto;
import com.vertoedu.dto.ApiResponse;
import com.vertoedu.service.AcademicYearService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/academic-years")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminAcademicYearController {

    private final AcademicYearService academicYearService;

    @GetMapping("/school/{schoolId}")
    public ApiResponse<List<AcademicYearDto>> getAcademicYearsBySchool(@PathVariable Long schoolId) {
        return ApiResponse.success("Academic years retrieved successfully", academicYearService.getAcademicYearsBySchool(schoolId));
    }

    @GetMapping("/{id}")
    public ApiResponse<AcademicYearDto> getAcademicYearById(@PathVariable Long id) {
        return ApiResponse.success("Academic year retrieved successfully", academicYearService.getAcademicYearById(id));
    }

    @PostMapping
    public ApiResponse<AcademicYearDto> createAcademicYear(@Valid @RequestBody AcademicYearDto dto) {
        return ApiResponse.success("Academic year created successfully", academicYearService.createAcademicYear(dto));
    }

    @PutMapping("/{id}")
    public ApiResponse<AcademicYearDto> updateAcademicYear(@PathVariable Long id, @Valid @RequestBody AcademicYearDto dto) {
        return ApiResponse.success("Academic year updated successfully", academicYearService.updateAcademicYear(id, dto));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteAcademicYear(@PathVariable Long id) {
        academicYearService.deleteAcademicYear(id);
        return ApiResponse.success("Academic year deleted successfully");
    }
}
