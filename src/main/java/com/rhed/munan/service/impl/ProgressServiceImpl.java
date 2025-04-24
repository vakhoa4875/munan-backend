package com.rhed.munan.service.impl;

import com.rhed.munan.exception.ExceptionUtils;
import com.rhed.munan.model.Lesson;
import com.rhed.munan.model.User;
import com.rhed.munan.model.UserProgress;
import com.rhed.munan.model.UserProgress.ProgressStatus;
import com.rhed.munan.repository.LessonRepository;
import com.rhed.munan.repository.UserProgressRepository;
import com.rhed.munan.repository.UserRepository;
import com.rhed.munan.service.ProgressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProgressServiceImpl implements ProgressService {

    private final UserProgressRepository progressRepository;
    private final UserRepository userRepository;
    private final LessonRepository lessonRepository;

    @Override
    @Transactional
    public UserProgress startLesson(UUID userId, UUID lessonId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> ExceptionUtils.notFound("User", "id", userId));

        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> ExceptionUtils.notFound("Lesson", "id", lessonId));

        Optional<UserProgress> existingProgress = progressRepository.findByUserAndLesson(user, lesson);

        if (existingProgress.isPresent()) {
            UserProgress progress = existingProgress.get();
            if (progress.getStatus() == ProgressStatus.NOT_STARTED) {
                progress.setStatus(ProgressStatus.IN_PROGRESS);
                progress.setStartedAt(LocalDateTime.now());
                return progressRepository.save(progress);
            }
            return progress;
        } else {
            UserProgress newProgress = new UserProgress();
            newProgress.setUser(user);
            newProgress.setLesson(lesson);
            newProgress.setStatus(ProgressStatus.IN_PROGRESS);
            newProgress.setStartedAt(LocalDateTime.now());
            return progressRepository.save(newProgress);
        }
    }

    @Override
    @Transactional
    public UserProgress completeLesson(UUID userId, UUID lessonId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> ExceptionUtils.notFound("User", "id", userId));

        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> ExceptionUtils.notFound("Lesson", "id", lessonId));

        Optional<UserProgress> existingProgress = progressRepository.findByUserAndLesson(user, lesson);

        if (existingProgress.isPresent()) {
            UserProgress progress = existingProgress.get();
            progress.setStatus(ProgressStatus.COMPLETED);
            progress.setCompletedAt(LocalDateTime.now());
            return progressRepository.save(progress);
        } else {
            UserProgress newProgress = new UserProgress();
            newProgress.setUser(user);
            newProgress.setLesson(lesson);
            newProgress.setStatus(ProgressStatus.COMPLETED);
            newProgress.setStartedAt(LocalDateTime.now());
            newProgress.setCompletedAt(LocalDateTime.now());
            return progressRepository.save(newProgress);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserProgress> getProgressByUserAndLesson(UUID userId, UUID lessonId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> ExceptionUtils.notFound("User", "id", userId));

        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> ExceptionUtils.notFound("Lesson", "id", lessonId));

        return progressRepository.findByUserAndLesson(user, lesson);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserProgress> getUserProgressForCourse(UUID userId, UUID courseId) {
        if (!userRepository.existsById(userId)) {
            throw ExceptionUtils.notFound("User", "id", userId);
        }

        if (!lessonRepository.existsByCourseId(courseId)) {
            throw ExceptionUtils.notFound("Course", "id", courseId);
        }

        return progressRepository.findByUserIdAndCourseId(userId, courseId);
    }

    @Override
    @Transactional(readOnly = true)
    public double calculateCourseCompletionPercentage(UUID userId, UUID courseId) {
        if (!userRepository.existsById(userId)) {
            throw ExceptionUtils.notFound("User", "id", userId);
        }

        Long totalLessons = lessonRepository.countLessonsByCourseId(courseId);
        if (totalLessons == 0) {
            return 0.0;
        }

        Long completedLessons = progressRepository.countCompletedLessonsByUserIdAndCourseId(userId, courseId);
        return (double) completedLessons / totalLessons * 100;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isLessonCompletedByUser(UUID userId, UUID lessonId) {
        if (!userRepository.existsById(userId)) {
            throw ExceptionUtils.notFound("User", "id", userId);
        }

        if (!lessonRepository.existsById(lessonId)) {
            throw ExceptionUtils.notFound("Lesson", "id", lessonId);
        }

        return progressRepository.isLessonCompletedByUser(userId, lessonId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserProgress> getAllUserProgress(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> ExceptionUtils.notFound("User", "id", userId));

        return progressRepository.findByUser(user);
    }
}