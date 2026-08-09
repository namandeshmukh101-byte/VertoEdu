package com.vertoedu.repository;

import com.vertoedu.entity.DocumentUpload;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vertoedu.entity.enums.DocumentStatus;

@Repository
public interface DocumentUploadRepository extends JpaRepository<DocumentUpload, Long> {
    long countByStatus(DocumentStatus status);
}
