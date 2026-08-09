package com.vertoedu.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SectionDto {
    private Long id;

    @NotNull(message = "School Class ID is required")
    private Long schoolClassId;

    @NotBlank(message = "Section name is required")
    private String name;
}
