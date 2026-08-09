package com.vertoedu.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SchoolDto {
    private Long id;

    @NotBlank(message = "School name is required")
    private String name;

    private String address;

    @Email(message = "Invalid email format")
    private String contactEmail;
}
