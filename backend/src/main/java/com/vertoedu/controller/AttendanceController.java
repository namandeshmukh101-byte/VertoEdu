package com.vertoedu.controller;

import com.vertoedu.dto.ApiResponse;
import com.vertoedu.dto.AttendanceRecordDto;
import com.vertoedu.service.AttendanceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/teacher/attendance")
@PreAuthorize("hasRole('TEACHER')")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;
    private final com.vertoedu.repository.UserRepository userRepository;

    @GetMapping
    public ResponseEntity<ApiResponse<List<AttendanceRecordDto>>> getAttendance(
            @RequestParam Long sectionId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(ApiResponse.success("Attendance fetched", 
                attendanceService.getAttendanceBySectionAndDate(sectionId, date)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<AttendanceRecordDto>> saveAttendance(
            @Valid @RequestBody AttendanceRecordDto dto,
            Authentication authentication) {
        Long userId = getUserId(authentication);
        return ResponseEntity.ok(ApiResponse.success("Attendance recorded", 
                attendanceService.saveAttendance(dto, userId)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AttendanceRecordDto>> updateAttendance(
            @PathVariable Long id,
            @Valid @RequestBody AttendanceRecordDto dto,
            Authentication authentication) {
        Long userId = getUserId(authentication);
        return ResponseEntity.ok(ApiResponse.success("Attendance updated", 
                attendanceService.updateAttendance(id, dto, userId)));
    }

    private Long getUserId(Authentication authentication) {
        return userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new com.vertoedu.exception.ResourceNotFoundException("User not found"))
                .getId();
    }
}
