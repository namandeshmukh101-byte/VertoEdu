package com.vertoedu.controller;

import com.vertoedu.dto.ApiResponse;
import com.vertoedu.dto.SchoolClassDto;
import com.vertoedu.service.SchoolClassService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/classes")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminClassController {

    private final SchoolClassService schoolClassService;

    @GetMapping("/school/{schoolId}")
    public ApiResponse<List<SchoolClassDto>> getClassesBySchool(@PathVariable Long schoolId) {
        return ApiResponse.success("Classes retrieved successfully", schoolClassService.getClassesBySchool(schoolId));
    }

    @GetMapping("/{id}")
    public ApiResponse<SchoolClassDto> getClassById(@PathVariable Long id) {
        return ApiResponse.success("Class retrieved successfully", schoolClassService.getClassById(id));
    }

    @PostMapping
    public ApiResponse<SchoolClassDto> createClass(@Valid @RequestBody SchoolClassDto dto) {
        return ApiResponse.success("Class created successfully", schoolClassService.createClass(dto));
    }

    @PutMapping("/{id}")
    public ApiResponse<SchoolClassDto> updateClass(@PathVariable Long id, @Valid @RequestBody SchoolClassDto dto) {
        return ApiResponse.success("Class updated successfully", schoolClassService.updateClass(id, dto));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteClass(@PathVariable Long id) {
        schoolClassService.deleteClass(id);
        return ApiResponse.success("Class deleted successfully");
    }
}
