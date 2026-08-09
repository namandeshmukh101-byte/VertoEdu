package com.vertoedu.repository;

import com.vertoedu.entity.ParentStudentLinkRequest;
import com.vertoedu.entity.enums.LinkRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParentStudentLinkRequestRepository extends JpaRepository<ParentStudentLinkRequest, Long> {
    List<ParentStudentLinkRequest> findByParentId(Long parentId);
    List<ParentStudentLinkRequest> findByStatus(LinkRequestStatus status);
    Optional<ParentStudentLinkRequest> findByParentIdAndScholarNumber(Long parentId, String scholarNumber);
}
