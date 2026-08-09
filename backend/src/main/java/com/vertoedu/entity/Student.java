package com.vertoedu.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

/**
 * Student entity — Represents a student enrolled in a school.
 */
@Entity
@Table(name = "students", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"school_id", "scholar_number"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Student extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id", nullable = false)
    private School school;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Parent parent;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(name = "scholar_number", nullable = false)
    private String scholarNumber;

    @Column
    private LocalDate dob;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "section_id")
    private Section schoolClass; // Naming it schoolClass to match my earlier repo method name, or I can rename the repo method.
}
