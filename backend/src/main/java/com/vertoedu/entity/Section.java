package com.vertoedu.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Section entity — Represents a section of a school class (e.g., "A", "B").
 */
@Entity
@Table(name = "sections", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"school_class_id", "name"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Section extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_class_id", nullable = false)
    private SchoolClass schoolClass;

    @Column(nullable = false)
    private String name;
}
