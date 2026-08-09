package com.vertoedu.repository;

import com.vertoedu.entity.Exam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamRepository extends JpaRepository<Exam, Long> {
    List<Exam> findByAcademicYearId(Long academicYearId);
    boolean existsByNameAndAcademicYearId(String name, Long academicYearId);
}
