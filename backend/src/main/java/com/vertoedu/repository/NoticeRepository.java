package com.vertoedu.repository;

import com.vertoedu.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, Long> {
    List<Notice> findTop10BySchoolIdOrderByCreatedAtDesc(Long schoolId);
}
