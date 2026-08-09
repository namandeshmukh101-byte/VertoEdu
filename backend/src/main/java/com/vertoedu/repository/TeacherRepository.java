package com.vertoedu.repository;

import com.vertoedu.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    boolean existsBySchoolIdAndEmployeeId(Long schoolId, String employeeId);
    List<Teacher> findBySchoolId(Long schoolId);
    java.util.Optional<Teacher> findByUserId(Long userId);
}
