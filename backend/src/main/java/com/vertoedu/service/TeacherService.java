package com.vertoedu.service;

import com.vertoedu.dto.TeacherDto;
import com.vertoedu.entity.School;
import com.vertoedu.entity.Teacher;
import com.vertoedu.entity.User;
import com.vertoedu.exception.DuplicateResourceException;
import com.vertoedu.exception.ResourceNotFoundException;
import com.vertoedu.repository.SchoolRepository;
import com.vertoedu.repository.TeacherRepository;
import com.vertoedu.repository.UserRepository;
import com.vertoedu.repository.SectionRepository;
import com.vertoedu.repository.SubjectRepository;
import com.vertoedu.entity.Section;
import com.vertoedu.entity.Subject;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashSet;
import java.util.Set;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeacherService {

    private final TeacherRepository teacherRepository;
    private final SchoolRepository schoolRepository;
    private final UserRepository userRepository;
    private final SectionRepository sectionRepository;
    private final SubjectRepository subjectRepository;

    @Transactional(readOnly = true)
    public List<TeacherDto> getTeachersBySchool(Long schoolId) {
        return teacherRepository.findBySchoolId(schoolId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TeacherDto getTeacherById(Long id) {
        return mapToDto(findTeacher(id));
    }

    @Transactional
    public TeacherDto createTeacher(TeacherDto dto) {
        if (dto.getEmployeeId() != null && !dto.getEmployeeId().isEmpty() &&
            teacherRepository.existsBySchoolIdAndEmployeeId(dto.getSchoolId(), dto.getEmployeeId())) {
            throw new DuplicateResourceException("Teacher with Employee ID '" + dto.getEmployeeId() + "' already exists in this school.");
        }

        School school = schoolRepository.findById(dto.getSchoolId())
                .orElseThrow(() -> new ResourceNotFoundException("School not found"));

        Teacher teacher = new Teacher();
        teacher.setSchool(school);
        
        if (dto.getUserId() != null) {
            User user = userRepository.findById(dto.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));
            teacher.setUser(user);
        }

        teacher.setFirstName(dto.getFirstName());
        teacher.setLastName(dto.getLastName());
        teacher.setPhone(dto.getPhone());
        teacher.setEmployeeId(dto.getEmployeeId());
        
        if (dto.getAssignedSectionIds() != null) {
            teacher.setAssignedSections(new HashSet<>(sectionRepository.findAllById(dto.getAssignedSectionIds())));
        }
        if (dto.getAssignedSubjectIds() != null) {
            teacher.setAssignedSubjects(new HashSet<>(subjectRepository.findAllById(dto.getAssignedSubjectIds())));
        }
        
        return mapToDto(teacherRepository.save(teacher));
    }

    @Transactional
    public TeacherDto updateTeacher(Long id, TeacherDto dto) {
        Teacher teacher = findTeacher(id);
        
        if (dto.getEmployeeId() != null && !dto.getEmployeeId().isEmpty() &&
            !dto.getEmployeeId().equals(teacher.getEmployeeId()) && 
            teacherRepository.existsBySchoolIdAndEmployeeId(teacher.getSchool().getId(), dto.getEmployeeId())) {
            throw new DuplicateResourceException("Teacher with Employee ID '" + dto.getEmployeeId() + "' already exists in this school.");
        }
        
        if (dto.getUserId() != null) {
            User user = userRepository.findById(dto.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));
            teacher.setUser(user);
        } else {
            teacher.setUser(null);
        }

        teacher.setFirstName(dto.getFirstName());
        teacher.setLastName(dto.getLastName());
        teacher.setPhone(dto.getPhone());
        teacher.setEmployeeId(dto.getEmployeeId());
        
        if (dto.getAssignedSectionIds() != null) {
            teacher.setAssignedSections(new HashSet<>(sectionRepository.findAllById(dto.getAssignedSectionIds())));
        } else {
            teacher.setAssignedSections(new HashSet<>());
        }
        if (dto.getAssignedSubjectIds() != null) {
            teacher.setAssignedSubjects(new HashSet<>(subjectRepository.findAllById(dto.getAssignedSubjectIds())));
        } else {
            teacher.setAssignedSubjects(new HashSet<>());
        }
        
        return mapToDto(teacherRepository.save(teacher));
    }

    @Transactional
    public void deleteTeacher(Long id) {
        teacherRepository.delete(findTeacher(id));
    }

    private Teacher findTeacher(Long id) {
        return teacherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found with ID: " + id));
    }

    private TeacherDto mapToDto(Teacher teacher) {
        TeacherDto dto = new TeacherDto();
        dto.setId(teacher.getId());
        dto.setSchoolId(teacher.getSchool().getId());
        dto.setUserId(teacher.getUser() != null ? teacher.getUser().getId() : null);
        dto.setFirstName(teacher.getFirstName());
        dto.setLastName(teacher.getLastName());
        dto.setPhone(teacher.getPhone());
        dto.setEmployeeId(teacher.getEmployeeId());
        
        if (teacher.getAssignedSections() != null) {
            dto.setAssignedSectionIds(teacher.getAssignedSections().stream().map(Section::getId).collect(Collectors.toSet()));
        }
        if (teacher.getAssignedSubjects() != null) {
            dto.setAssignedSubjectIds(teacher.getAssignedSubjects().stream().map(Subject::getId).collect(Collectors.toSet()));
        }
        return dto;
    }
}
