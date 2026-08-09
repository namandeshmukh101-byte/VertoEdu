package com.vertoedu.service;

import com.vertoedu.dto.ExamResultDto;
import com.vertoedu.entity.ExamResult;
import com.vertoedu.repository.ExamResultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ResultQueryService {

    private final ExamResultRepository examResultRepository;

    @Transactional(readOnly = true)
    public List<ExamResultDto> getStudentExamResults(Long studentId) {
        List<ExamResult> results = examResultRepository.findByStudentId(studentId);
        return results.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    private ExamResultDto mapToDto(ExamResult result) {
        return ExamResultDto.builder()
                .id(result.getId())
                .studentId(result.getStudent().getId())
                .studentName(result.getStudent().getFirstName() + " " + result.getStudent().getLastName())
                .scholarNumber(result.getStudent().getScholarNumber())
                .subjectId(result.getSubject().getId())
                .subjectName(result.getSubject().getName())
                .examId(result.getExam().getId())
                .examName(result.getExam().getName())
                .marksObtained(result.getMarksObtained())
                .maxMarks(result.getMaxMarks())
                .remarks(result.getRemarks())
                .recordedById(result.getRecordedBy().getId())
                .build();
    }
}
