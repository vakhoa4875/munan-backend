package com.rhed.munan.repository;

import com.rhed.munan.model.Course;
import com.rhed.munan.model.Purchase;
import com.rhed.munan.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Long> {

    List<Purchase> findByUser(User user);

    List<Purchase> findByCourse(Course course);

    boolean existsByUserAndCourse(User user, Course course);

    boolean existsByUserAndCourseAndPaymentVerifiedTrue(User user, Course course);

    @Query("SELECT COUNT(p) FROM Purchase p WHERE p.course.id = :courseId AND p.paymentVerified = true")
    Long countVerifiedPurchasesByCourseId(@Param("courseId") UUID courseId);

    @Query("SELECT p FROM Purchase p WHERE p.createdAt BETWEEN :startDate AND :endDate ORDER BY p.createdAt DESC")
    List<Purchase> findPurchasesInDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
}