package com.vertoedu.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "ai_reviews")
@Getter
@Setter
public class AIReview extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ocr_result_id", nullable = false, unique = true)
    private OCRResult ocrResult;

    @Column(columnDefinition = "TEXT")
    private String suggestionsText;

    @Column(columnDefinition = "JSON", nullable = false)
    private String suggestedDataJson;

}
