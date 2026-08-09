package com.vertoedu.repository;

import com.vertoedu.entity.Parent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

import java.util.Optional;

@Repository
public interface ParentRepository extends JpaRepository<Parent, Long> {
    List<Parent> findBySchoolId(Long schoolId);
    Optional<Parent> findByUserId(Long userId);
    Optional<Parent> findByUserEmail(String email);
}
