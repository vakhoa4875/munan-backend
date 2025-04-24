package com.rhed.munan.repository;

import com.rhed.munan.model.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, UUID> {

    List<Lesson> findByCourseIdOrderByOrderAsc(UUID courseId);

    @Query("SELECT COUNT(l) FROM Lesson l WHERE l.course.id = :courseId")
    Long countLessonsByCourseId(@Param("courseId") UUID courseId);

    @Query("SELECT SUM(l.duration) FROM Lesson l WHERE l.course.id = :courseId")
    Integer getTotalDurationByCourseId(@Param("courseId") UUID courseId);

    boolean existsByCourseId(UUID courseId);
}