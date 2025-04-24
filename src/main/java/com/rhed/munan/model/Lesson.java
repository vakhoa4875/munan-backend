package com.rhed.munan.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "lessons")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Lesson extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "\"order\"")
    private Integer order;

    @Column(name = "duration")
    private Integer duration;

    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL)
    private Set<UserProgress> progress = new HashSet<>();
}