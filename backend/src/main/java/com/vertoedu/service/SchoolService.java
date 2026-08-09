package com.vertoedu.service;

import com.vertoedu.dto.SchoolDto;
import com.vertoedu.entity.School;
import com.vertoedu.exception.DuplicateResourceException;
import com.vertoedu.exception.ResourceNotFoundException;
import com.vertoedu.repository.SchoolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SchoolService {

    private final SchoolRepository schoolRepository;

    @Transactional(readOnly = true)
    public List<SchoolDto> getAllSchools() {
        return schoolRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public SchoolDto getSchoolById(Long id) {
        return mapToDto(findSchool(id));
    }

    @Transactional
    public SchoolDto createSchool(SchoolDto dto) {
        if (schoolRepository.existsByName(dto.getName())) {
            throw new DuplicateResourceException("School with name '" + dto.getName() + "' already exists.");
        }
        School school = new School();
        school.setName(dto.getName());
        school.setAddress(dto.getAddress());
        school.setContactEmail(dto.getContactEmail());
        
        return mapToDto(schoolRepository.save(school));
    }

    @Transactional
    public SchoolDto updateSchool(Long id, SchoolDto dto) {
        School school = findSchool(id);
        
        if (!school.getName().equals(dto.getName()) && schoolRepository.existsByName(dto.getName())) {
            throw new DuplicateResourceException("School with name '" + dto.getName() + "' already exists.");
        }

        school.setName(dto.getName());
        school.setAddress(dto.getAddress());
        school.setContactEmail(dto.getContactEmail());
        
        return mapToDto(schoolRepository.save(school));
    }

    @Transactional
    public void deleteSchool(Long id) {
        schoolRepository.delete(findSchool(id));
    }

    private School findSchool(Long id) {
        return schoolRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("School not found with ID: " + id));
    }

    private SchoolDto mapToDto(School school) {
        SchoolDto dto = new SchoolDto();
        dto.setId(school.getId());
        dto.setName(school.getName());
        dto.setAddress(school.getAddress());
        dto.setContactEmail(school.getContactEmail());
        return dto;
    }
}
