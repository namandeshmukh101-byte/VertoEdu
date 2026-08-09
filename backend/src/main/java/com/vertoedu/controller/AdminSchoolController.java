package com.vertoedu.controller;

import com.vertoedu.dto.ApiResponse;
import com.vertoedu.dto.SchoolDto;
import com.vertoedu.service.SchoolService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/schools")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminSchoolController {

    private final SchoolService schoolService;

    @GetMapping
    public ApiResponse<List<SchoolDto>> getAllSchools() {
        return ApiResponse.success("Schools retrieved successfully", schoolService.getAllSchools());
    }

    @GetMapping("/{id}")
    public ApiResponse<SchoolDto> getSchoolById(@PathVariable Long id) {
        return ApiResponse.success("School retrieved successfully", schoolService.getSchoolById(id));
    }

    @PostMapping
    public ApiResponse<SchoolDto> createSchool(@Valid @RequestBody SchoolDto dto) {
        return ApiResponse.success("School created successfully", schoolService.createSchool(dto));
    }

    @PutMapping("/{id}")
    public ApiResponse<SchoolDto> updateSchool(@PathVariable Long id, @Valid @RequestBody SchoolDto dto) {
        return ApiResponse.success("School updated successfully", schoolService.updateSchool(id, dto));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteSchool(@PathVariable Long id) {
        schoolService.deleteSchool(id);
        return ApiResponse.success("School deleted successfully");
    }
}
