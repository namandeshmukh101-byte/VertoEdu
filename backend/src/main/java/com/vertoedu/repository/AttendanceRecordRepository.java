package com.vertoedu.repository;

import com.vertoedu.entity.AttendanceRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRecordRepository extends JpaRepository<AttendanceRecord, Long> {
    
    List<AttendanceRecord> findByStudent_SchoolClass_IdAndDate(Long sectionId, LocalDate date);
    
    Optional<AttendanceRecord> findByStudentIdAndDate(Long studentId, LocalDate date);
    
    boolean existsByStudentIdAndDate(Long studentId, LocalDate date);
    
    List<AttendanceRecord> findByStudentIdOrderByDateDesc(Long studentId);
    
    List<AttendanceRecord> findByStudentIdAndDateBetweenOrderByDateDesc(Long studentId, LocalDate startDate, LocalDate endDate);
}
