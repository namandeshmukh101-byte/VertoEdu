package com.vertoedu.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "approval_logs")
@Getter
@Setter
public class ApprovalLog extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_upload_id", nullable = false)
    private DocumentUpload documentUpload;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_user_id", nullable = false)
    private User adminUser;

    @Column(nullable = false)
    private boolean isApproved;

    @Column(columnDefinition = "JSON")
    private String finalApprovedDataJson;

    @Column(columnDefinition = "TEXT")
    private String rejectionReason;
}
