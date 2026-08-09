package com.vertoedu.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class AcademicYearDto {
    private Long id;

    @NotNull(message = "School ID is required")
    private Long schoolId;

    @NotBlank(message = "Academic year name is required")
    private String name;

    private LocalDate startDate;
    private LocalDate endDate;
    
    private Boolean isActive;
}
