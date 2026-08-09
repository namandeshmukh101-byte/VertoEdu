package com.vertoedu.repository;

import com.vertoedu.entity.AcademicYear;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AcademicYearRepository extends JpaRepository<AcademicYear, Long> {
    boolean existsBySchoolIdAndName(Long schoolId, String name);
    List<AcademicYear> findBySchoolId(Long schoolId);
}
