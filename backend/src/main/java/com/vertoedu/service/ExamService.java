package com.vertoedu.service;

import com.vertoedu.dto.ExamDto;
import com.vertoedu.entity.AcademicYear;
import com.vertoedu.entity.Exam;
import com.vertoedu.entity.School;
import com.vertoedu.exception.DuplicateResourceException;
import com.vertoedu.exception.ResourceNotFoundException;
import com.vertoedu.repository.AcademicYearRepository;
import com.vertoedu.repository.ExamRepository;
import com.vertoedu.repository.SchoolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExamService {

    private final ExamRepository examRepository;
    private final AcademicYearRepository academicYearRepository;
    private final SchoolRepository schoolRepository;

    @Transactional(readOnly = true)
    public List<ExamDto> getExamsByAcademicYear(Long academicYearId) {
        return examRepository.findByAcademicYearId(academicYearId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public ExamDto createExam(ExamDto dto) {
        if (examRepository.existsByNameAndAcademicYearId(dto.getName(), dto.getAcademicYearId())) {
            throw new DuplicateResourceException("Exam with this name already exists in this academic year.");
        }

        School school = schoolRepository.findById(dto.getSchoolId())
                .orElseThrow(() -> new ResourceNotFoundException("School not found"));
        AcademicYear year = academicYearRepository.findById(dto.getAcademicYearId())
                .orElseThrow(() -> new ResourceNotFoundException("Academic Year not found"));

        Exam exam = Exam.builder()
                .school(school)
                .academicYear(year)
                .name(dto.getName())
                .build();

        return mapToDto(examRepository.save(exam));
    }

    private ExamDto mapToDto(Exam exam) {
        return ExamDto.builder()
                .id(exam.getId())
                .schoolId(exam.getSchool().getId())
                .academicYearId(exam.getAcademicYear().getId())
                .name(exam.getName())
                .build();
    }
}
