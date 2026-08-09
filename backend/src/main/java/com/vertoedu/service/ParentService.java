package com.vertoedu.service;

import com.vertoedu.dto.ParentDto;
import com.vertoedu.entity.Parent;
import com.vertoedu.entity.School;
import com.vertoedu.entity.User;
import com.vertoedu.dto.ParentProfileUpdateDto;
import com.vertoedu.dto.StudentProfileDto;
import com.vertoedu.entity.Student;
import com.vertoedu.exception.ResourceNotFoundException;
import com.vertoedu.repository.ParentRepository;
import com.vertoedu.repository.SchoolRepository;
import com.vertoedu.repository.StudentRepository;
import com.vertoedu.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ParentService {

    private final ParentRepository parentRepository;
    private final SchoolRepository schoolRepository;
    private final UserRepository userRepository;
    private final StudentRepository studentRepository;

    @Transactional(readOnly = true)
    public List<ParentDto> getParentsBySchool(Long schoolId) {
        return parentRepository.findBySchoolId(schoolId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ParentDto getParentById(Long id) {
        return mapToDto(findParent(id));
    }

    @Transactional
    public ParentDto createParent(ParentDto dto) {
        School school = schoolRepository.findById(dto.getSchoolId())
                .orElseThrow(() -> new ResourceNotFoundException("School not found"));

        Parent parent = new Parent();
        parent.setSchool(school);
        
        if (dto.getUserId() != null) {
            User user = userRepository.findById(dto.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));
            parent.setUser(user);
        }

        parent.setFirstName(dto.getFirstName());
        parent.setLastName(dto.getLastName());
        parent.setPhone(dto.getPhone());
        parent.setAddress(dto.getAddress());
        
        return mapToDto(parentRepository.save(parent));
    }

    @Transactional
    public ParentDto updateParent(Long id, ParentDto dto) {
        Parent parent = findParent(id);
        
        if (dto.getUserId() != null) {
            User user = userRepository.findById(dto.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));
            parent.setUser(user);
        } else {
            parent.setUser(null);
        }

        parent.setFirstName(dto.getFirstName());
        parent.setLastName(dto.getLastName());
        parent.setPhone(dto.getPhone());
        parent.setAddress(dto.getAddress());
        
        return mapToDto(parentRepository.save(parent));
    }

    @Transactional
    public void deleteParent(Long id) {
        parentRepository.delete(findParent(id));
    }

    @Transactional(readOnly = true)
    public Parent getParentEntityByUserEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new com.vertoedu.exception.ResourceNotFoundException("User not found"));
        return parentRepository.findByUserId(user.getId())
                .orElseThrow(() -> new com.vertoedu.exception.ResourceNotFoundException("Parent profile not found"));
    }

    @Transactional(readOnly = true)
    public ParentDto getParentProfile(String email) {
        Parent parent = getParentEntityByUserEmail(email);
        return mapToDto(parent);
    }

    @Transactional
    public ParentDto updateParentProfile(String email, ParentProfileUpdateDto updateDto) {
        Parent parent = getParentEntityByUserEmail(email);
        
        if (updateDto.getPhone() != null) parent.setPhone(updateDto.getPhone());
        if (updateDto.getAlternateContact() != null) parent.setAlternateContact(updateDto.getAlternateContact());
        if (updateDto.getAddress() != null) parent.setAddress(updateDto.getAddress());
        
        parent = parentRepository.save(parent);
        return mapToDto(parent);
    }

    @Transactional(readOnly = true)
    public List<StudentProfileDto> getLinkedStudents(String email) {
        Parent parent = getParentEntityByUserEmail(email);
        List<Student> students = studentRepository.findByParentId(parent.getId());
        return students.stream().map(this::mapStudentToDto).collect(Collectors.toList());
    }

    private Parent findParent(Long id) {
        return parentRepository.findById(id)
                .orElseThrow(() -> new com.vertoedu.exception.ResourceNotFoundException("Parent not found with ID: " + id));
    }

    private ParentDto mapToDto(Parent parent) {
        return ParentDto.builder()
                .id(parent.getId())
                .schoolId(parent.getSchool().getId())
                .userId(parent.getUser() != null ? parent.getUser().getId() : null)
                .firstName(parent.getFirstName())
                .lastName(parent.getLastName())
                .phone(parent.getPhone())
                .alternateContact(parent.getAlternateContact())
                .address(parent.getAddress())
                .email(parent.getUser() != null ? parent.getUser().getEmail() : null)
                .build();
    }

    private StudentProfileDto mapStudentToDto(Student student) {
        return StudentProfileDto.builder()
                .id(student.getId())
                .firstName(student.getFirstName())
                .lastName(student.getLastName())
                .scholarNumber(student.getScholarNumber())
                .dob(student.getDob())
                .className(student.getSchoolClass() != null ? student.getSchoolClass().getSchoolClass().getName() : "")
                .sectionName(student.getSchoolClass() != null ? student.getSchoolClass().getName() : "")
                .build();
    }
}
