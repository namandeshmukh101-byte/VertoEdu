package com.vertoedu.repository;

import com.vertoedu.entity.ExamResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExamResultRepository extends JpaRepository<ExamResult, Long> {
    
    List<ExamResult> findByStudent_SchoolClass_IdAndSubjectIdAndExamId(Long sectionId, Long subjectId, Long examId);
    
    Optional<ExamResult> findByStudentIdAndSubjectIdAndExamId(Long studentId, Long subjectId, Long examId);
    
    boolean existsByStudentIdAndSubjectIdAndExamId(Long studentId, Long subjectId, Long examId);
    
    List<ExamResult> findByStudentId(Long studentId);
}
