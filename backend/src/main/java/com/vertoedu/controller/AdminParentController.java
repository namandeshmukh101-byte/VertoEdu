package com.vertoedu.controller;

import com.vertoedu.dto.ApiResponse;
import com.vertoedu.dto.ParentDto;
import com.vertoedu.service.ParentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/parents")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminParentController {

    private final ParentService parentService;

    @GetMapping("/school/{schoolId}")
    public ApiResponse<List<ParentDto>> getParentsBySchool(@PathVariable Long schoolId) {
        return ApiResponse.success("Parents retrieved successfully", parentService.getParentsBySchool(schoolId));
    }

    @GetMapping("/{id}")
    public ApiResponse<ParentDto> getParentById(@PathVariable Long id) {
        return ApiResponse.success("Parent retrieved successfully", parentService.getParentById(id));
    }

    @PostMapping
    public ApiResponse<ParentDto> createParent(@Valid @RequestBody ParentDto dto) {
        return ApiResponse.success("Parent created successfully", parentService.createParent(dto));
    }

    @PutMapping("/{id}")
    public ApiResponse<ParentDto> updateParent(@PathVariable Long id, @Valid @RequestBody ParentDto dto) {
        return ApiResponse.success("Parent updated successfully", parentService.updateParent(id, dto));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteParent(@PathVariable Long id) {
        parentService.deleteParent(id);
        return ApiResponse.success("Parent deleted successfully");
    }
}
