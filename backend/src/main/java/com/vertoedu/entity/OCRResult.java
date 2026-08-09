package com.vertoedu.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "ocr_results")
@Getter
@Setter
public class OCRResult extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_upload_id", nullable = false, unique = true)
    private DocumentUpload documentUpload;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String rawText;

    @Column(columnDefinition = "JSON", nullable = false)
    private String extractedDataJson;
}
