package com.vertoedu.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Teacher entity — Represents a teacher in the system.
 */
@Entity
@Table(name = "teachers", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"school_id", "employee_id"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Teacher extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id", nullable = false)
    private School school;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column
    private String phone;

    @Column(name = "employee_id")
    private String employeeId;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "teacher_sections",
        joinColumns = @JoinColumn(name = "teacher_id"),
        inverseJoinColumns = @JoinColumn(name = "section_id")
    )
    private java.util.Set<Section> assignedSections;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "teacher_subjects",
        joinColumns = @JoinColumn(name = "teacher_id"),
        inverseJoinColumns = @JoinColumn(name = "subject_id")
    )
    private java.util.Set<Subject> assignedSubjects;
}
