package com.vertoedu.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExamDto {
    private Long id;

    @NotNull(message = "School ID is required")
    private Long schoolId;

    @NotNull(message = "Academic Year ID is required")
    private Long academicYearId;

    @NotBlank(message = "Exam name is required")
    private String name;
}
