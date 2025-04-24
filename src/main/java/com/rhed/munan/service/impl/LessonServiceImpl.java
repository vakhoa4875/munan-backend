package com.rhed.munan.service.impl;

import com.rhed.munan.exception.BadRequestException;
import com.rhed.munan.exception.ExceptionUtils;
import com.rhed.munan.model.Course;
import com.rhed.munan.model.Lesson;
import com.rhed.munan.repository.CourseRepository;
import com.rhed.munan.repository.LessonRepository;
import com.rhed.munan.service.LessonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class LessonServiceImpl implements LessonService {

    private final LessonRepository lessonRepository;
    private final CourseRepository courseRepository;

    @Override
    @Transactional
    public Lesson createLesson(Lesson lesson, UUID courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> ExceptionUtils.notFound("Course", "id", courseId));

        lesson.setCourse(course);

        // Validate duration
        if (lesson.getDuration() != null && lesson.getDuration() < 0) {
            throw ExceptionUtils.badRequest("Lesson duration cannot be negative");
        }

        // Set order to the end if not specified
        if (lesson.getOrder() == null) {
            Long count = lessonRepository.countLessonsByCourseId(courseId);
            lesson.setOrder(count.intValue() + 1);
        }

        return lessonRepository.save(lesson);
    }

    @Override
    @Transactional
    public Lesson updateLesson(Lesson lesson) {
        // Verify lesson exists
        Lesson existingLesson = lessonRepository.findById(lesson.getId())
                .orElseThrow(() -> ExceptionUtils.notFound("Lesson", "id", lesson.getId()));

        // Preserve the course
        lesson.setCourse(existingLesson.getCourse());

        // Validate duration
        if (lesson.getDuration() != null && lesson.getDuration() < 0) {
            throw ExceptionUtils.badRequest("Lesson duration cannot be negative");
        }

        return lessonRepository.save(lesson);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Lesson> getLessonById(UUID id) {
        return lessonRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Lesson> getLessonsByCourse(UUID courseId) {
        if (!courseRepository.existsById(courseId)) {
            throw ExceptionUtils.notFound("Course", "id", courseId);
        }
        return lessonRepository.findByCourseIdOrderByOrderAsc(courseId);
    }

    @Override
    @Transactional
    public void deleteLesson(UUID id) {
        if (!lessonRepository.existsById(id)) {
            throw ExceptionUtils.notFound("Lesson", "id", id);
        }
        lessonRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void reorderLessons(UUID courseId, List<UUID> orderedLessonIds) {
        if (!courseRepository.existsById(courseId)) {
            throw ExceptionUtils.notFound("Course", "id", courseId);
        }

        List<Lesson> lessons = lessonRepository.findByCourseIdOrderByOrderAsc(courseId);

        // Validate that all lessons belong to the course
        if (lessons.size() != orderedLessonIds.size()) {
            throw ExceptionUtils.badRequest("Invalid lesson list for reordering");
        }

        // Check if all provided lesson IDs belong to this course
        for (UUID lessonId : orderedLessonIds) {
            boolean found = lessons.stream().anyMatch(lesson -> lesson.getId().equals(lessonId));
            if (!found) {
                throw ExceptionUtils.badRequest("Lesson with ID " + lessonId + " does not belong to course " + courseId);
            }
        }

        // Update order for each lesson
        IntStream.range(0, orderedLessonIds.size()).forEach(i -> {
            UUID lessonId = orderedLessonIds.get(i);
            lessons.stream()
                    .filter(lesson -> lesson.getId().equals(lessonId))
                    .findFirst()
                    .ifPresent(lesson -> {
                        lesson.setOrder(i + 1);
                        lessonRepository.save(lesson);
                    });
        });
    }

    @Override
    @Transactional(readOnly = true)
    public Long countLessonsByCourse(UUID courseId) {
        if (!courseRepository.existsById(courseId)) {
            throw ExceptionUtils.notFound("Course", "id", courseId);
        }
        return lessonRepository.countLessonsByCourseId(courseId);
    }

    @Override
    @Transactional(readOnly = true)
    public Integer getTotalCourseDuration(UUID courseId) {
        if (!courseRepository.existsById(courseId)) {
            throw ExceptionUtils.notFound("Course", "id", courseId);
        }
        Integer duration = lessonRepository.getTotalDurationByCourseId(courseId);
        return duration != null ? duration : 0;
    }
}