package com.vertoedu.service;

import com.vertoedu.dto.StudentDto;
import com.vertoedu.entity.Parent;
import com.vertoedu.entity.School;
import com.vertoedu.entity.Student;
import com.vertoedu.exception.DuplicateResourceException;
import com.vertoedu.exception.ResourceNotFoundException;
import com.vertoedu.repository.ParentRepository;
import com.vertoedu.repository.SchoolRepository;
import com.vertoedu.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final SchoolRepository schoolRepository;
    private final ParentRepository parentRepository;

    @Transactional(readOnly = true)
    public List<StudentDto> getStudentsBySchool(Long schoolId) {
        return studentRepository.findBySchoolId(schoolId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public StudentDto getStudentById(Long id) {
        return mapToDto(findStudent(id));
    }

    @Transactional
    public StudentDto createStudent(StudentDto dto) {
        if (studentRepository.existsBySchoolIdAndScholarNumber(dto.getSchoolId(), dto.getScholarNumber())) {
            throw new DuplicateResourceException("Student with Admission Number '" + dto.getScholarNumber() + "' already exists in this school.");
        }

        School school = schoolRepository.findById(dto.getSchoolId())
                .orElseThrow(() -> new ResourceNotFoundException("School not found"));

        Student student = new Student();
        student.setSchool(school);
        
        if (dto.getParentId() != null) {
            Parent parent = parentRepository.findById(dto.getParentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Parent not found"));
            student.setParent(parent);
        }

        student.setFirstName(dto.getFirstName());
        student.setLastName(dto.getLastName());
        student.setScholarNumber(dto.getScholarNumber());
        student.setDob(dto.getDob());
        
        return mapToDto(studentRepository.save(student));
    }

    @Transactional
    public StudentDto updateStudent(Long id, StudentDto dto) {
        Student student = findStudent(id);
        
        if (!student.getScholarNumber().equals(dto.getScholarNumber()) && 
            studentRepository.existsBySchoolIdAndScholarNumber(student.getSchool().getId(), dto.getScholarNumber())) {
            throw new DuplicateResourceException("Student with Admission Number '" + dto.getScholarNumber() + "' already exists in this school.");
        }
        
        if (dto.getParentId() != null) {
            Parent parent = parentRepository.findById(dto.getParentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Parent not found"));
            student.setParent(parent);
        } else {
            student.setParent(null);
        }

        student.setFirstName(dto.getFirstName());
        student.setLastName(dto.getLastName());
        student.setScholarNumber(dto.getScholarNumber());
        student.setDob(dto.getDob());
        
        return mapToDto(studentRepository.save(student));
    }

    @Transactional
    public void deleteStudent(Long id) {
        studentRepository.delete(findStudent(id));
    }

    private Student findStudent(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + id));
    }

    private StudentDto mapToDto(Student student) {
        StudentDto dto = new StudentDto();
        dto.setId(student.getId());
        dto.setSchoolId(student.getSchool().getId());
        dto.setParentId(student.getParent() != null ? student.getParent().getId() : null);
        dto.setFirstName(student.getFirstName());
        dto.setLastName(student.getLastName());
        dto.setScholarNumber(student.getScholarNumber());
        dto.setDob(student.getDob());
        return dto;
    }
}
