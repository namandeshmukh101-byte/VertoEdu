package com.vertoedu.controller;

import com.vertoedu.dto.ApiResponse;
import com.vertoedu.dto.StudentDto;
import com.vertoedu.entity.Student;
import com.vertoedu.entity.Teacher;
import com.vertoedu.entity.Section;
import com.vertoedu.entity.Parent;
import com.vertoedu.repository.StudentRepository;
import com.vertoedu.repository.TeacherRepository;
import com.vertoedu.repository.ParentRepository;
import com.vertoedu.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
public class SearchController {

    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final ParentRepository parentRepository;
    private final UserRepository userRepository;

    @GetMapping("/students")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'PARENT')")
    public ResponseEntity<ApiResponse<StudentDto>> searchStudentByScholarNumber(
            @RequestParam String scholarNumber,
            Authentication authentication) {

        Student student = studentRepository.findByScholarNumber(scholarNumber)
                .orElseThrow(() -> new com.vertoedu.exception.ResourceNotFoundException("Student not found"));

        String email = authentication.getName();
        com.vertoedu.entity.User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new com.vertoedu.exception.ResourceNotFoundException("User not found"));

        if (user.getRole().getName().equals("TEACHER")) {
            Teacher teacher = teacherRepository.findAll().stream()
                    .filter(t -> t.getUser().getId().equals(user.getId()))
                    .findFirst()
                    .orElseThrow(() -> new com.vertoedu.exception.ResourceNotFoundException("Teacher profile not found"));
            
            boolean hasAccess = false;
            for (Section s : teacher.getAssignedSections()) {
                if (s.getId().equals(student.getSchoolClass().getId())) {
                    hasAccess = true;
                    break;
                }
            }
            if (!hasAccess) {
                throw new org.springframework.security.access.AccessDeniedException("You are not authorized to view data for this student");
            }
        } else if (user.getRole().getName().equals("PARENT")) {
            Parent parent = parentRepository.findAll().stream()
                    .filter(p -> p.getUser().getId().equals(user.getId()))
                    .findFirst()
                    .orElseThrow(() -> new com.vertoedu.exception.ResourceNotFoundException("Parent profile not found"));
            
            if (student.getParent() == null || !student.getParent().getId().equals(parent.getId())) {
                throw new org.springframework.security.access.AccessDeniedException("You are not authorized to view data for this student");
            }
        }

        StudentDto dto = new StudentDto();
        dto.setId(student.getId());
        dto.setSchoolId(student.getSchool().getId());
        dto.setFirstName(student.getFirstName());
        dto.setLastName(student.getLastName());
        dto.setScholarNumber(student.getScholarNumber());
        dto.setDob(student.getDob());
        dto.setParentId(student.getParent() != null ? student.getParent().getId() : null);

        return ResponseEntity.ok(ApiResponse.success("Student retrieved successfully", dto));
    }
}
