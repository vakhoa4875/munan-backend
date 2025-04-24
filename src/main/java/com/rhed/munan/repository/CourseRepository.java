package com.rhed.munan.repository;

import com.rhed.munan.model.Course;
import com.rhed.munan.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CourseRepository extends JpaRepository<Course, UUID> {

    List<Course> findByCreatedBy(User creator);

    Page<Course> findByPublishedTrue(Pageable pageable);

    @Query("SELECT c FROM Course c WHERE c.published = true AND LOWER(c.title) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Course> searchByTitleContaining(@Param("keyword") String keyword, Pageable pageable);

    Page<Course> findByPriceLessThanEqual(Integer maxPrice, Pageable pageable);

    @Query("SELECT c FROM Course c JOIN Purchase p ON c.id = p.course.id WHERE p.user.id = :userId AND p.paymentVerified = true")
    List<Course> findPurchasedCoursesByUserId(@Param("userId") UUID userId);
}