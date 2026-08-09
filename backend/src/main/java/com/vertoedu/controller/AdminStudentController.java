package com.vertoedu.controller;

import com.vertoedu.dto.ApiResponse;
import com.vertoedu.dto.StudentDto;
import com.vertoedu.service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/students")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminStudentController {

    private final StudentService studentService;

    @GetMapping("/school/{schoolId}")
    public ApiResponse<List<StudentDto>> getStudentsBySchool(@PathVariable Long schoolId) {
        return ApiResponse.success("Students retrieved successfully", studentService.getStudentsBySchool(schoolId));
    }

    @GetMapping("/{id}")
    public ApiResponse<StudentDto> getStudentById(@PathVariable Long id) {
        return ApiResponse.success("Student retrieved successfully", studentService.getStudentById(id));
    }

    @PostMapping
    public ApiResponse<StudentDto> createStudent(@Valid @RequestBody StudentDto dto) {
        return ApiResponse.success("Student created successfully", studentService.createStudent(dto));
    }

    @PutMapping("/{id}")
    public ApiResponse<StudentDto> updateStudent(@PathVariable Long id, @Valid @RequestBody StudentDto dto) {
        return ApiResponse.success("Student updated successfully", studentService.updateStudent(id, dto));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ApiResponse.success("Student deleted successfully");
    }
}
