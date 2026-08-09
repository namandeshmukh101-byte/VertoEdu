package com.vertoedu.service;

import com.vertoedu.dto.SectionDto;
import com.vertoedu.entity.SchoolClass;
import com.vertoedu.entity.Section;
import com.vertoedu.exception.DuplicateResourceException;
import com.vertoedu.exception.ResourceNotFoundException;
import com.vertoedu.repository.SchoolClassRepository;
import com.vertoedu.repository.SectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SectionService {

    private final SectionRepository sectionRepository;
    private final SchoolClassRepository schoolClassRepository;

    @Transactional(readOnly = true)
    public List<SectionDto> getSectionsByClass(Long classId) {
        return sectionRepository.findBySchoolClassId(classId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public SectionDto getSectionById(Long id) {
        return mapToDto(findSection(id));
    }

    @Transactional
    public SectionDto createSection(SectionDto dto) {
        if (sectionRepository.existsBySchoolClassIdAndName(dto.getSchoolClassId(), dto.getName())) {
            throw new DuplicateResourceException("Section '" + dto.getName() + "' already exists in this class.");
        }
        
        SchoolClass schoolClass = schoolClassRepository.findById(dto.getSchoolClassId())
                .orElseThrow(() -> new ResourceNotFoundException("Class not found"));

        Section section = new Section();
        section.setSchoolClass(schoolClass);
        section.setName(dto.getName());
        
        return mapToDto(sectionRepository.save(section));
    }

    @Transactional
    public SectionDto updateSection(Long id, SectionDto dto) {
        Section section = findSection(id);
        
        if (!section.getName().equals(dto.getName()) && 
            sectionRepository.existsBySchoolClassIdAndName(section.getSchoolClass().getId(), dto.getName())) {
            throw new DuplicateResourceException("Section '" + dto.getName() + "' already exists in this class.");
        }

        section.setName(dto.getName());
        
        return mapToDto(sectionRepository.save(section));
    }

    @Transactional
    public void deleteSection(Long id) {
        sectionRepository.delete(findSection(id));
    }

    private Section findSection(Long id) {
        return sectionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Section not found with ID: " + id));
    }

    private SectionDto mapToDto(Section section) {
        SectionDto dto = new SectionDto();
        dto.setId(section.getId());
        dto.setSchoolClassId(section.getSchoolClass().getId());
        dto.setName(section.getName());
        return dto;
    }
}
