package com.vertoedu.service;

import com.vertoedu.dto.SectionDto;
import com.vertoedu.dto.StudentDto;
import com.vertoedu.dto.SubjectDto;
import com.vertoedu.entity.Section;
import com.vertoedu.entity.Student;
import com.vertoedu.entity.Subject;
import com.vertoedu.entity.Teacher;
import com.vertoedu.exception.ResourceNotFoundException;
import com.vertoedu.repository.StudentRepository;
import com.vertoedu.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeacherDashboardService {

    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;
    private final com.vertoedu.repository.AttendanceRecordRepository attendanceRecordRepository;
    private final com.vertoedu.repository.ExamRepository examRepository;

    @Transactional(readOnly = true)
    public java.util.Map<String, Object> getDashboardSummary(Long userId) {
        Teacher teacher = getTeacher(userId);
        java.util.Map<String, Object> summary = new java.util.HashMap<>();
        
        long classesCount = teacher.getAssignedSections().size();
        
        // Find if attendance is recorded for any assigned section today
        boolean attendanceDoneToday = teacher.getAssignedSections().stream()
                .anyMatch(section -> !attendanceRecordRepository.findByStudent_SchoolClass_IdAndDate(section.getId(), java.time.LocalDate.now()).isEmpty());
        
        long pendingExams = examRepository.count(); // Simplified for prototype: all exams
        
        summary.put("classesCount", classesCount);
        summary.put("attendanceDoneToday", attendanceDoneToday);
        summary.put("pendingExamsCount", pendingExams);
        
        return summary;
    }

    @Transactional(readOnly = true)
    public List<SectionDto> getAssignedSections(Long userId) {
        Teacher teacher = getTeacher(userId);
        return teacher.getAssignedSections().stream()
                .map(this::mapSection)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<SubjectDto> getAssignedSubjects(Long userId) {
        Teacher teacher = getTeacher(userId);
        return teacher.getAssignedSubjects().stream()
                .map(this::mapSubject)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<StudentDto> getStudentsForSection(Long userId, Long sectionId) {
        Teacher teacher = getTeacher(userId);
        
        // Security check: ensure teacher is assigned to this section
        boolean isAssigned = teacher.getAssignedSections().stream()
                .anyMatch(s -> s.getId().equals(sectionId));
                
        if (!isAssigned) {
            throw new org.springframework.security.access.AccessDeniedException("You are not assigned to this class.");
        }

        return studentRepository.findBySchoolClassId(sectionId).stream()
                .map(this::mapStudent)
                .collect(Collectors.toList());
    }

    private Teacher getTeacher(Long userId) {
        return teacherRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher profile not found for the current user."));
    }

    private SectionDto mapSection(Section section) {
        return SectionDto.builder()
                .id(section.getId())
                .schoolClassId(section.getSchoolClass().getId())
                .name(section.getName())
                // Assuming we might need class name + section name
                // .className(section.getSchoolClass().getName()) - We would add this to SectionDto if needed.
                .build();
    }

    private SubjectDto mapSubject(Subject subject) {
        return SubjectDto.builder()
                .id(subject.getId())
                .schoolId(subject.getSchool().getId())
                .name(subject.getName())
                .code(subject.getCode())
                .description(subject.getDescription())
                .build();
    }

    private StudentDto mapStudent(Student student) {
        return StudentDto.builder()
                .id(student.getId())
                .schoolId(student.getSchool().getId())
                .firstName(student.getFirstName())
                .lastName(student.getLastName())
                .scholarNumber(student.getScholarNumber())
                .dob(student.getDob())
                .build();
    }
}
