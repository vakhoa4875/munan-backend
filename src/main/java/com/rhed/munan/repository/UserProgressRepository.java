package com.rhed.munan.repository;

import com.rhed.munan.model.Lesson;
import com.rhed.munan.model.User;
import com.rhed.munan.model.UserProgress;
import com.rhed.munan.model.UserProgress.ProgressStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserProgressRepository extends JpaRepository<UserProgress, Long> {

    Optional<UserProgress> findByUserAndLesson(User user, Lesson lesson);

    List<UserProgress> findByUser(User user);

    @Query("SELECT up FROM UserProgress up JOIN up.lesson l WHERE up.user.id = :userId AND l.course.id = :courseId")
    List<UserProgress> findByUserIdAndCourseId(@Param("userId") UUID userId, @Param("courseId") UUID courseId);

    @Query("SELECT COUNT(up) FROM UserProgress up JOIN up.lesson l WHERE up.user.id = :userId AND l.course.id = :courseId AND up.status = 'COMPLETED'")
    Long countCompletedLessonsByUserIdAndCourseId(@Param("userId") UUID userId, @Param("courseId") UUID courseId);

    @Query("SELECT CASE WHEN COUNT(up) > 0 THEN true ELSE false END FROM UserProgress up WHERE up.user.id = :userId AND up.lesson.id = :lessonId AND up.status = 'COMPLETED'")
    boolean isLessonCompletedByUser(@Param("userId") UUID userId, @Param("lessonId") UUID lessonId);
}