package com.vertoedu.service;

import com.vertoedu.dto.AcademicYearDto;
import com.vertoedu.entity.AcademicYear;
import com.vertoedu.entity.School;
import com.vertoedu.exception.DuplicateResourceException;
import com.vertoedu.exception.ResourceNotFoundException;
import com.vertoedu.repository.AcademicYearRepository;
import com.vertoedu.repository.SchoolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AcademicYearService {

    private final AcademicYearRepository academicYearRepository;
    private final SchoolRepository schoolRepository;

    @Transactional(readOnly = true)
    public List<AcademicYearDto> getAcademicYearsBySchool(Long schoolId) {
        return academicYearRepository.findBySchoolId(schoolId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public AcademicYearDto getAcademicYearById(Long id) {
        return mapToDto(findAcademicYear(id));
    }

    @Transactional
    public AcademicYearDto createAcademicYear(AcademicYearDto dto) {
        if (academicYearRepository.existsBySchoolIdAndName(dto.getSchoolId(), dto.getName())) {
            throw new DuplicateResourceException("Academic year '" + dto.getName() + "' already exists for this school.");
        }
        
        School school = schoolRepository.findById(dto.getSchoolId())
                .orElseThrow(() -> new ResourceNotFoundException("School not found"));

        AcademicYear year = new AcademicYear();
        year.setSchool(school);
        year.setName(dto.getName());
        year.setStartDate(dto.getStartDate());
        year.setEndDate(dto.getEndDate());
        year.setIsActive(dto.getIsActive() != null ? dto.getIsActive() : false);
        
        return mapToDto(academicYearRepository.save(year));
    }

    @Transactional
    public AcademicYearDto updateAcademicYear(Long id, AcademicYearDto dto) {
        AcademicYear year = findAcademicYear(id);
        
        if (!year.getName().equals(dto.getName()) && 
            academicYearRepository.existsBySchoolIdAndName(year.getSchool().getId(), dto.getName())) {
            throw new DuplicateResourceException("Academic year '" + dto.getName() + "' already exists for this school.");
        }

        year.setName(dto.getName());
        year.setStartDate(dto.getStartDate());
        year.setEndDate(dto.getEndDate());
        year.setIsActive(dto.getIsActive() != null ? dto.getIsActive() : year.getIsActive());
        
        return mapToDto(academicYearRepository.save(year));
    }

    @Transactional
    public void deleteAcademicYear(Long id) {
        academicYearRepository.delete(findAcademicYear(id));
    }

    private AcademicYear findAcademicYear(Long id) {
        return academicYearRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Academic year not found with ID: " + id));
    }

    private AcademicYearDto mapToDto(AcademicYear year) {
        AcademicYearDto dto = new AcademicYearDto();
        dto.setId(year.getId());
        dto.setSchoolId(year.getSchool().getId());
        dto.setName(year.getName());
        dto.setStartDate(year.getStartDate());
        dto.setEndDate(year.getEndDate());
        dto.setIsActive(year.getIsActive());
        return dto;
    }
}
