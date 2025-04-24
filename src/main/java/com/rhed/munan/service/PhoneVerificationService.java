package com.rhed.munan.service;

import com.rhed.munan.model.PhoneVerification;
import com.rhed.munan.model.PhoneVerification.ZaloStatus;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PhoneVerificationService {

    PhoneVerification requestVerification(UUID userId, String phoneNumber);

    PhoneVerification updateVerificationStatus(Long verificationId, ZaloStatus status);

    @Transactional
    PhoneVerification completeVerification(Long verificationId);

    @Transactional
    PhoneVerification failVerification(Long verificationId, String reason);

    Optional<PhoneVerification> getVerificationById(Long id);

    List<PhoneVerification> getVerificationsByUser(UUID userId);

    boolean isPhoneNumberVerified(UUID userId, String phoneNumber);

    void cleanupExpiredVerifications();

    boolean hasReachedDailyVerificationLimit(UUID userId);

    @Transactional(readOnly = true)
    Optional<PhoneVerification> getLatestVerificationByUser(UUID userId);

    @Transactional(readOnly = true)
    List<PhoneVerification> getPendingVerifications();
}