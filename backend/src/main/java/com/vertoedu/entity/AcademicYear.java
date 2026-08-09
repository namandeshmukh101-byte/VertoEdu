package com.vertoedu.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

/**
 * AcademicYear entity — Represents a specific academic calendar year for a school.
 */
@Entity
@Table(name = "academic_years", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"school_id", "name"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AcademicYear extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id", nullable = false)
    private School school;

    @Column(nullable = false)
    private String name; // e.g., "2026-2027"

    @Column
    private LocalDate startDate;

    @Column
    private LocalDate endDate;

    @Column(nullable = false)
    @Builder.Default
    private Boolean isActive = false;
}
