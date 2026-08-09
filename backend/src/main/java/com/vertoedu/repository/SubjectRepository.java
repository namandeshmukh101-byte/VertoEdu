package com.vertoedu.repository;

import com.vertoedu.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {
    boolean existsBySchoolIdAndCode(Long schoolId, String code);
    List<Subject> findBySchoolId(Long schoolId);
}
