package com.vertoedu.service;

import com.vertoedu.dto.SchoolClassDto;
import com.vertoedu.entity.School;
import com.vertoedu.entity.SchoolClass;
import com.vertoedu.exception.DuplicateResourceException;
import com.vertoedu.exception.ResourceNotFoundException;
import com.vertoedu.repository.SchoolClassRepository;
import com.vertoedu.repository.SchoolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SchoolClassService {

    private final SchoolClassRepository schoolClassRepository;
    private final SchoolRepository schoolRepository;

    @Transactional(readOnly = true)
    public List<SchoolClassDto> getClassesBySchool(Long schoolId) {
        return schoolClassRepository.findBySchoolId(schoolId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public SchoolClassDto getClassById(Long id) {
        return mapToDto(findSchoolClass(id));
    }

    @Transactional
    public SchoolClassDto createClass(SchoolClassDto dto) {
        if (schoolClassRepository.existsBySchoolIdAndName(dto.getSchoolId(), dto.getName())) {
            throw new DuplicateResourceException("Class '" + dto.getName() + "' already exists in this school.");
        }
        
        School school = schoolRepository.findById(dto.getSchoolId())
                .orElseThrow(() -> new ResourceNotFoundException("School not found"));

        SchoolClass schoolClass = new SchoolClass();
        schoolClass.setSchool(school);
        schoolClass.setName(dto.getName());
        schoolClass.setLevel(dto.getLevel());
        
        return mapToDto(schoolClassRepository.save(schoolClass));
    }

    @Transactional
    public SchoolClassDto updateClass(Long id, SchoolClassDto dto) {
        SchoolClass schoolClass = findSchoolClass(id);
        
        if (!schoolClass.getName().equals(dto.getName()) && 
            schoolClassRepository.existsBySchoolIdAndName(schoolClass.getSchool().getId(), dto.getName())) {
            throw new DuplicateResourceException("Class '" + dto.getName() + "' already exists in this school.");
        }

        schoolClass.setName(dto.getName());
        schoolClass.setLevel(dto.getLevel());
        
        return mapToDto(schoolClassRepository.save(schoolClass));
    }

    @Transactional
    public void deleteClass(Long id) {
        schoolClassRepository.delete(findSchoolClass(id));
    }

    private SchoolClass findSchoolClass(Long id) {
        return schoolClassRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Class not found with ID: " + id));
    }

    private SchoolClassDto mapToDto(SchoolClass schoolClass) {
        SchoolClassDto dto = new SchoolClassDto();
        dto.setId(schoolClass.getId());
        dto.setSchoolId(schoolClass.getSchool().getId());
        dto.setName(schoolClass.getName());
        dto.setLevel(schoolClass.getLevel());
        return dto;
    }
}
