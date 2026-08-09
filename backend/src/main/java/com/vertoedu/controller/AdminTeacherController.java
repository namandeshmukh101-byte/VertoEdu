package com.vertoedu.controller;

import com.vertoedu.dto.ApiResponse;
import com.vertoedu.dto.TeacherDto;
import com.vertoedu.service.TeacherService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/teachers")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminTeacherController {

    private final TeacherService teacherService;

    @GetMapping("/school/{schoolId}")
    public ApiResponse<List<TeacherDto>> getTeachersBySchool(@PathVariable Long schoolId) {
        return ApiResponse.success("Teachers retrieved successfully", teacherService.getTeachersBySchool(schoolId));
    }

    @GetMapping("/{id}")
    public ApiResponse<TeacherDto> getTeacherById(@PathVariable Long id) {
        return ApiResponse.success("Teacher retrieved successfully", teacherService.getTeacherById(id));
    }

    @PostMapping
    public ApiResponse<TeacherDto> createTeacher(@Valid @RequestBody TeacherDto dto) {
        return ApiResponse.success("Teacher created successfully", teacherService.createTeacher(dto));
    }

    @PutMapping("/{id}")
    public ApiResponse<TeacherDto> updateTeacher(@PathVariable Long id, @Valid @RequestBody TeacherDto dto) {
        return ApiResponse.success("Teacher updated successfully", teacherService.updateTeacher(id, dto));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteTeacher(@PathVariable Long id) {
        teacherService.deleteTeacher(id);
        return ApiResponse.success("Teacher deleted successfully");
    }
}
