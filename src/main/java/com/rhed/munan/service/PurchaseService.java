package com.rhed.munan.service;

import com.rhed.munan.model.Purchase;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PurchaseService {

    Purchase createPurchase(UUID userId, UUID courseId, String paymentMethod);

    boolean verifyPurchase(Long purchaseId, UUID verifierId);

    Optional<Purchase> getPurchaseById(Long id);

    List<Purchase> getPurchasesByUser(UUID userId);

    List<Purchase> getPurchasesByCourse(UUID courseId);

    boolean hasPurchasedCourse(UUID userId, UUID courseId);

    Long countPurchasesForCourse(UUID courseId);

    List<Purchase> getPurchasesInDateRange(LocalDateTime startDate, LocalDateTime endDate);
}