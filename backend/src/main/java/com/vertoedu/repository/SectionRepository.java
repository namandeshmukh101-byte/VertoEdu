package com.vertoedu.repository;

import com.vertoedu.entity.Section;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SectionRepository extends JpaRepository<Section, Long> {
    boolean existsBySchoolClassIdAndName(Long schoolClassId, String name);
    List<Section> findBySchoolClassId(Long schoolClassId);
}
