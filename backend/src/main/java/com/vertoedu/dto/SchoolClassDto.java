package com.vertoedu.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SchoolClassDto {
    private Long id;

    @NotNull(message = "School ID is required")
    private Long schoolId;

    @NotBlank(message = "Class name is required")
    private String name;

    private String level;
}
