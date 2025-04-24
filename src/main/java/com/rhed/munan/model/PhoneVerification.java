package com.rhed.munan.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "phone_verifications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PhoneVerification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "zalo_status")
    private ZaloStatus zaloStatus = ZaloStatus.PENDING;

    @Column(name = "requested_at")
    private LocalDateTime requestedAt = LocalDateTime.now();

    @Column(name = "verified_at")
    private LocalDateTime verifiedAt;

    public enum ZaloStatus {
        PENDING, VERIFIED, FAILED
    }
}