package com.rhed.munan.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BaseEntity {

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "full_name")
    private String fullName;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private UserRole role = UserRole.USER;

    @Column(name = "phone_verified")
    private boolean phoneVerified = false;

    @OneToMany(mappedBy = "createdBy", cascade = CascadeType.ALL)
    private Set<Course> courses = new HashSet<>();

    @OneToMany(mappedBy = "createdBy", cascade = CascadeType.ALL)
    private Set<Blog> blogs = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<UserProgress> progress = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Purchase> purchases = new HashSet<>();

    public enum UserRole {
        USER, OWNER
    }
}