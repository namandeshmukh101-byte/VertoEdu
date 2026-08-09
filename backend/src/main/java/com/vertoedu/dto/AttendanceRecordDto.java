package com.vertoedu.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceRecordDto {
    private Long id;

    @NotNull(message = "Student ID is required")
    private Long studentId;
    
    private String studentName;
    private String scholarNumber;

    @NotNull(message = "Date is required")
    private LocalDate date;

    @NotNull(message = "Status is required")
    private String status; // PRESENT or ABSENT

    private String remarks;
    
    private Long recordedById;
    private String recordedByName;
}
