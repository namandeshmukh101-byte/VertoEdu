package com.vertoedu.service;

import com.vertoedu.dto.SubjectDto;
import com.vertoedu.entity.School;
import com.vertoedu.entity.Subject;
import com.vertoedu.exception.DuplicateResourceException;
import com.vertoedu.exception.ResourceNotFoundException;
import com.vertoedu.repository.SchoolRepository;
import com.vertoedu.repository.SubjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubjectService {

    private final SubjectRepository subjectRepository;
    private final SchoolRepository schoolRepository;

    @Transactional(readOnly = true)
    public List<SubjectDto> getSubjectsBySchool(Long schoolId) {
        return subjectRepository.findBySchoolId(schoolId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public SubjectDto getSubjectById(Long id) {
        return mapToDto(findSubject(id));
    }

    @Transactional
    public SubjectDto createSubject(SubjectDto dto) {
        if (subjectRepository.existsBySchoolIdAndCode(dto.getSchoolId(), dto.getCode())) {
            throw new DuplicateResourceException("Subject with code '" + dto.getCode() + "' already exists in this school.");
        }
        
        School school = schoolRepository.findById(dto.getSchoolId())
                .orElseThrow(() -> new ResourceNotFoundException("School not found"));

        Subject subject = new Subject();
        subject.setSchool(school);
        subject.setName(dto.getName());
        subject.setCode(dto.getCode());
        subject.setDescription(dto.getDescription());
        
        return mapToDto(subjectRepository.save(subject));
    }

    @Transactional
    public SubjectDto updateSubject(Long id, SubjectDto dto) {
        Subject subject = findSubject(id);
        
        if (!subject.getCode().equals(dto.getCode()) && 
            subjectRepository.existsBySchoolIdAndCode(subject.getSchool().getId(), dto.getCode())) {
            throw new DuplicateResourceException("Subject with code '" + dto.getCode() + "' already exists in this school.");
        }

        subject.setName(dto.getName());
        subject.setCode(dto.getCode());
        subject.setDescription(dto.getDescription());
        
        return mapToDto(subjectRepository.save(subject));
    }

    @Transactional
    public void deleteSubject(Long id) {
        subjectRepository.delete(findSubject(id));
    }

    private Subject findSubject(Long id) {
        return subjectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found with ID: " + id));
    }

    private SubjectDto mapToDto(Subject subject) {
        SubjectDto dto = new SubjectDto();
        dto.setId(subject.getId());
        dto.setSchoolId(subject.getSchool().getId());
        dto.setName(subject.getName());
        dto.setCode(subject.getCode());
        dto.setDescription(subject.getDescription());
        return dto;
    }
}
