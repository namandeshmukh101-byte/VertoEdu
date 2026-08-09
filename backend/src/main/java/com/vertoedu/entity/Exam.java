package com.vertoedu.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "exams", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"academic_year_id", "name"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Exam extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id", nullable = false)
    private School school;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "academic_year_id", nullable = false)
    private AcademicYear academicYear;

    @Column(nullable = false)
    private String name; // PT-1, Half-Yearly, Final, etc.
}
