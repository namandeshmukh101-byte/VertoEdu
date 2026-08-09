package com.vertoedu.controller;

import com.vertoedu.dto.ApiResponse;
import com.vertoedu.dto.SectionDto;
import com.vertoedu.service.SectionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/sections")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminSectionController {

    private final SectionService sectionService;

    @GetMapping("/class/{classId}")
    public ApiResponse<List<SectionDto>> getSectionsByClass(@PathVariable Long classId) {
        return ApiResponse.success("Sections retrieved successfully", sectionService.getSectionsByClass(classId));
    }

    @GetMapping("/{id}")
    public ApiResponse<SectionDto> getSectionById(@PathVariable Long id) {
        return ApiResponse.success("Section retrieved successfully", sectionService.getSectionById(id));
    }

    @PostMapping
    public ApiResponse<SectionDto> createSection(@Valid @RequestBody SectionDto dto) {
        return ApiResponse.success("Section created successfully", sectionService.createSection(dto));
    }

    @PutMapping("/{id}")
    public ApiResponse<SectionDto> updateSection(@PathVariable Long id, @Valid @RequestBody SectionDto dto) {
        return ApiResponse.success("Section updated successfully", sectionService.updateSection(id, dto));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteSection(@PathVariable Long id) {
        sectionService.deleteSection(id);
        return ApiResponse.success("Section deleted successfully");
    }
}
