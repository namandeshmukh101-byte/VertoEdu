package com.vertoedu.repository;

import com.vertoedu.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    boolean existsBySchoolIdAndScholarNumber(Long schoolId, String scholarNumber);
    List<Student> findBySchoolId(Long schoolId);
    List<Student> findBySchoolClassId(Long sectionId);
    List<Student> findByParentId(Long parentId);
    List<Student> findTop5ByOrderByCreatedAtDesc();
    java.util.Optional<Student> findByScholarNumber(String scholarNumber);
}
