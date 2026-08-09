package com.vertoedu.repository;

import com.vertoedu.entity.SchoolClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SchoolClassRepository extends JpaRepository<SchoolClass, Long> {
    boolean existsBySchoolIdAndName(Long schoolId, String name);
    List<SchoolClass> findBySchoolId(Long schoolId);
}
