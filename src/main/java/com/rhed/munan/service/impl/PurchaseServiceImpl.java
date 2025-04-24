package com.rhed.munan.service.impl;

import com.rhed.munan.exception.ExceptionUtils;
import com.rhed.munan.model.Course;
import com.rhed.munan.model.Purchase;
import com.rhed.munan.model.User;
import com.rhed.munan.repository.CourseRepository;
import com.rhed.munan.repository.PurchaseRepository;
import com.rhed.munan.repository.UserRepository;
import com.rhed.munan.service.PurchaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PurchaseServiceImpl implements PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;

    @Override
    @Transactional
    public Purchase createPurchase(UUID userId, UUID courseId, String paymentMethod) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> ExceptionUtils.notFound("User", "id", userId));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> ExceptionUtils.notFound("Course", "id", courseId));

        // Check if user already purchased this course
        if (purchaseRepository.existsByUserAndCourse(user, course)) {
            throw ExceptionUtils.duplicate("Purchase", "courseId", courseId);
        }

        Purchase purchase = new Purchase();
        purchase.setUser(user);
        purchase.setCourse(course);
        purchase.setPaymentMethod(paymentMethod);
        purchase.setPaymentVerified(false);

        return purchaseRepository.save(purchase);
    }

    @Override
    @Transactional
    public boolean verifyPurchase(Long purchaseId, UUID verifierId) {
        Purchase purchase = purchaseRepository.findById(purchaseId)
                .orElseThrow(() -> ExceptionUtils.notFound("Purchase", "id", purchaseId));

        User verifier = userRepository.findById(verifierId)
                .orElseThrow(() -> ExceptionUtils.notFound("User", "id", verifierId));

        purchase.setPaymentVerified(true);
        purchase.setVerifiedBy(verifier);
        purchaseRepository.save(purchase);
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Purchase> getPurchaseById(Long id) {
        return purchaseRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Purchase> getPurchasesByUser(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> ExceptionUtils.notFound("User", "id", userId));

        return purchaseRepository.findByUser(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Purchase> getPurchasesByCourse(UUID courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> ExceptionUtils.notFound("Course", "id", courseId));

        return purchaseRepository.findByCourse(course);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasPurchasedCourse(UUID userId, UUID courseId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> ExceptionUtils.notFound("User", "id", userId));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> ExceptionUtils.notFound("Course", "id", courseId));

        return purchaseRepository.existsByUserAndCourseAndPaymentVerifiedTrue(user, course);
    }

    @Override
    @Transactional(readOnly = true)
    public Long countPurchasesForCourse(UUID courseId) {
        if (!courseRepository.existsById(courseId)) {
            throw ExceptionUtils.notFound("Course", "id", courseId);
        }
        return purchaseRepository.countVerifiedPurchasesByCourseId(courseId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Purchase> getPurchasesInDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate == null || endDate == null) {
            throw ExceptionUtils.badRequest("Start date and end date must be provided");
        }

        if (startDate.isAfter(endDate)) {
            throw ExceptionUtils.badRequest("Start date cannot be after end date");
        }

        return purchaseRepository.findPurchasesInDateRange(startDate, endDate);
    }
}