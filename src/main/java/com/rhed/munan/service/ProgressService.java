package com.rhed.munan.service;

import com.rhed.munan.model.UserProgress;
import com.rhed.munan.model.UserProgress.ProgressStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProgressService {

    UserProgress startLesson(UUID userId, UUID lessonId);

    UserProgress completeLesson(UUID userId, UUID lessonId);

    Optional<UserProgress> getProgressByUserAndLesson(UUID userId, UUID lessonId);

    List<UserProgress> getUserProgressForCourse(UUID userId, UUID courseId);

    double calculateCourseCompletionPercentage(UUID userId, UUID courseId);

    boolean isLessonCompletedByUser(UUID userId, UUID lessonId);

    List<UserProgress> getAllUserProgress(UUID userId);
}