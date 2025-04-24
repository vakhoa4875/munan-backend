package com.rhed.munan.service.impl;

import com.rhed.munan.exception.ExceptionUtils;
import com.rhed.munan.model.PhoneVerification;
import com.rhed.munan.model.PhoneVerification.ZaloStatus;
import com.rhed.munan.model.User;
import com.rhed.munan.repository.PhoneVerificationRepository;
import com.rhed.munan.repository.UserRepository;
import com.rhed.munan.service.PhoneVerificationService;
import com.rhed.munan.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class PhoneVerificationServiceImpl implements PhoneVerificationService {

    private final PhoneVerificationRepository verificationRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    // Simple regex for Vietnamese phone numbers
    private static final Pattern PHONE_PATTERN = Pattern.compile("^(0|\\+84)(3|5|7|8|9)\\d{8}$");

    @Override
    @Transactional
    public PhoneVerification requestVerification(UUID userId, String phoneNumber) {
        // Validate phone number format
        if (!isValidPhoneNumber(phoneNumber)) {
            throw ExceptionUtils.badRequest("Invalid phone number format");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> ExceptionUtils.notFound("User", "id", userId));

        // Check if there's a recent pending verification
        Optional<PhoneVerification> recentVerification = verificationRepository
                .findLatestByUserAndStatus(userId, ZaloStatus.PENDING);

        if (recentVerification.isPresent()) {
            PhoneVerification verification = recentVerification.get();
            LocalDateTime requestTime = verification.getRequestedAt();

            // If the request was made less than 5 minutes ago, don't allow a new request
            if (requestTime.plusMinutes(5).isAfter(LocalDateTime.now())) {
                throw ExceptionUtils.badRequest("Please wait before requesting another verification");
            }
        }

        // Create new verification request
        PhoneVerification verification = new PhoneVerification();
        verification.setUser(user);
        verification.setPhoneNumber(normalizePhoneNumber(phoneNumber));
        verification.setZaloStatus(ZaloStatus.PENDING);
        verification.setRequestedAt(LocalDateTime.now());

        return verificationRepository.save(verification);
    }

    @Override
    public PhoneVerification updateVerificationStatus(Long verificationId, ZaloStatus status) {
        return null;
    }

    @Transactional
    @Override
    public PhoneVerification completeVerification(Long verificationId) {
        PhoneVerification verification = verificationRepository.findById(verificationId)
                .orElseThrow(() -> ExceptionUtils.notFound("Verification", "id", verificationId));

        if (verification.getZaloStatus() != ZaloStatus.PENDING) {
            throw ExceptionUtils.badRequest("Verification is not in PENDING status");
        }

        verification.setZaloStatus(ZaloStatus.VERIFIED);
        verification.setVerifiedAt(LocalDateTime.now());

        // Update user's phone verification status
        userService.verifyUserPhone(verification.getUser().getId(), verification.getPhoneNumber());

        return verificationRepository.save(verification);
    }

    @Transactional
    @Override
    public PhoneVerification failVerification(Long verificationId, String reason) {
        PhoneVerification verification = verificationRepository.findById(verificationId)
                .orElseThrow(() -> ExceptionUtils.notFound("Verification", "id", verificationId));

        if (verification.getZaloStatus() != ZaloStatus.PENDING) {
            throw ExceptionUtils.badRequest("Verification is not in PENDING status");
        }

        verification.setZaloStatus(ZaloStatus.FAILED);
        // Could store the reason in a notes field if added to the model

        return verificationRepository.save(verification);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PhoneVerification> getVerificationById(Long id) {
        return verificationRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PhoneVerification> getVerificationsByUser(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> ExceptionUtils.notFound("User", "id", userId));

        return verificationRepository.findByUser(user);
    }

    @Override
    public boolean isPhoneNumberVerified(UUID userId, String phoneNumber) {
        return false;
    }

    @Override
    public void cleanupExpiredVerifications() {

    }

    @Override
    public boolean hasReachedDailyVerificationLimit(UUID userId) {
        return false;
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<PhoneVerification> getLatestVerificationByUser(UUID userId) {
        if (!userRepository.existsById(userId)) {
            throw ExceptionUtils.notFound("User", "id", userId);
        }

        return verificationRepository.findLatestByUser(userId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<PhoneVerification> getPendingVerifications() {
        return verificationRepository.findByZaloStatus(ZaloStatus.PENDING);
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            return false;
        }
        return PHONE_PATTERN.matcher(phoneNumber).matches();
    }

    private String normalizePhoneNumber(String phoneNumber) {
        // Remove spaces and other non-digit characters
        String normalized = phoneNumber.replaceAll("[^0-9+]", "");

        // Convert +84 format to 0 format if needed
        if (normalized.startsWith("+84")) {
            normalized = "0" + normalized.substring(3);
        }

        return normalized;
    }
}