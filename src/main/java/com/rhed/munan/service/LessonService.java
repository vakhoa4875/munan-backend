package com.rhed.munan.service;

import com.rhed.munan.model.Lesson;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LessonService {

    Lesson createLesson(Lesson lesson, UUID courseId);

    Lesson updateLesson(Lesson lesson);

    Optional<Lesson> getLessonById(UUID id);

    List<Lesson> getLessonsByCourse(UUID courseId);

    void deleteLesson(UUID id);

    void reorderLessons(UUID courseId, List<UUID> orderedLessonIds);

    Long countLessonsByCourse(UUID courseId);

    Integer getTotalCourseDuration(UUID courseId);
}