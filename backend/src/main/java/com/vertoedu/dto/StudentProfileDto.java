package com.vertoedu.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentProfileDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String scholarNumber;
    private LocalDate dob;
    private String className;
    private String sectionName;
}
