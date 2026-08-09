package com.vertoedu.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExamResultDto {
    private Long id;

    @NotNull(message = "Student ID is required")
    private Long studentId;
    
    private String studentName;
    private String scholarNumber;

    @NotNull(message = "Subject ID is required")
    private Long subjectId;
    
    private String subjectName;

    @NotNull(message = "Exam ID is required")
    private Long examId;
    
    private String examName;

    @NotNull(message = "Marks obtained is required")
    private Double marksObtained;

    @NotNull(message = "Maximum marks is required")
    private Double maxMarks;

    private String remarks;
    
    private Long recordedById;
}
