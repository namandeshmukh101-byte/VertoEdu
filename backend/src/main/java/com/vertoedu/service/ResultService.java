package com.vertoedu.service;

import com.vertoedu.dto.ExamResultDto;
import com.vertoedu.entity.Exam;
import com.vertoedu.entity.ExamResult;
import com.vertoedu.entity.Student;
import com.vertoedu.entity.Subject;
import com.vertoedu.entity.Teacher;
import com.vertoedu.exception.DuplicateResourceException;
import com.vertoedu.exception.ResourceNotFoundException;
import com.vertoedu.repository.ExamRepository;
import com.vertoedu.repository.ExamResultRepository;
import com.vertoedu.repository.StudentRepository;
import com.vertoedu.repository.SubjectRepository;
import com.vertoedu.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ResultService {

    private final ExamResultRepository resultRepository;
    private final StudentRepository studentRepository;
    private final SubjectRepository subjectRepository;
    private final ExamRepository examRepository;
    private final TeacherRepository teacherRepository;

    @Transactional(readOnly = true)
    public List<ExamResultDto> getResults(Long sectionId, Long subjectId, Long examId) {
        return resultRepository.findByStudent_SchoolClass_IdAndSubjectIdAndExamId(sectionId, subjectId, examId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public ExamResultDto saveResult(ExamResultDto dto, Long currentUserId) {
        if (resultRepository.existsByStudentIdAndSubjectIdAndExamId(dto.getStudentId(), dto.getSubjectId(), dto.getExamId())) {
            throw new DuplicateResourceException("Result already exists for this student, subject, and exam.");
        }
        
        if (dto.getMarksObtained() > dto.getMaxMarks() || dto.getMarksObtained() < 0) {
            throw new IllegalArgumentException("Invalid marks: Marks obtained must be between 0 and max marks.");
        }

        Student student = studentRepository.findById(dto.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));
        Subject subject = subjectRepository.findById(dto.getSubjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found"));
        Exam exam = examRepository.findById(dto.getExamId())
                .orElseThrow(() -> new ResourceNotFoundException("Exam not found"));
        Teacher teacher = teacherRepository.findByUserId(currentUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found"));

        ExamResult result = new ExamResult();
        result.setStudent(student);
        result.setSubject(subject);
        result.setExam(exam);
        result.setRecordedBy(teacher);
        result.setMarksObtained(dto.getMarksObtained());
        result.setMaxMarks(dto.getMaxMarks());
        result.setRemarks(dto.getRemarks());

        return mapToDto(resultRepository.save(result));
    }

    @Transactional
    public ExamResultDto updateResult(Long id, ExamResultDto dto, Long currentUserId) {
        ExamResult result = resultRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Exam result not found"));

        if (dto.getMarksObtained() > dto.getMaxMarks() || dto.getMarksObtained() < 0) {
            throw new IllegalArgumentException("Invalid marks: Marks obtained must be between 0 and max marks.");
        }

        Teacher teacher = teacherRepository.findByUserId(currentUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found"));

        result.setMarksObtained(dto.getMarksObtained());
        result.setMaxMarks(dto.getMaxMarks());
        result.setRemarks(dto.getRemarks());
        result.setRecordedBy(teacher);

        return mapToDto(resultRepository.save(result));
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
