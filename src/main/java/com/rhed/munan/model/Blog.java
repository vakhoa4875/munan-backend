package com.rhed.munan.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Type;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "blogs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Blog extends BaseEntity {

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "slug", unique = true)
    private String slug;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "excerpt")
    private String excerpt;

    @Column(name = "featured_image")
    private String featuredImage;

    @Column(name = "category")
    private String category;

    @Column(name = "tags", columnDefinition = "text[]")
    private String[] tags;

    @Column(name = "status")
    private String status = "draft";

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "views")
    private Integer views = 0;

    @Column(name = "votes")
    private Integer votes = 0;

    @OneToMany(mappedBy = "blog", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Comment> comments = new HashSet<>();

    @OneToMany(mappedBy = "blog", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Vote> votesList = new HashSet<>();
}