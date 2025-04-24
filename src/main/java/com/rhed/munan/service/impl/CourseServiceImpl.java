package com.rhed.munan.service.impl;

import com.rhed.munan.exception.BadRequestException;
import com.rhed.munan.exception.ExceptionUtils;
import com.rhed.munan.model.Course;
import com.rhed.munan.model.User;
import com.rhed.munan.repository.CourseRepository;
import com.rhed.munan.repository.UserRepository;
import com.rhed.munan.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public Course createCourse(Course course, UUID creatorId) {
        User creator = userRepository.findById(creatorId)
                .orElseThrow(() -> ExceptionUtils.notFound("User", "id", creatorId));

        if (course.getPrice() < 0) {
            throw ExceptionUtils.badRequest("Course price cannot be negative");
        }

        course.setCreatedBy(creator);
        course.setPublished(false);
        return courseRepository.save(course);
    }

    @Override
    @Transactional
    public Course updateCourse(Course course) {
        // Verify course exists
        Course existingCourse = courseRepository.findById(course.getId())
                .orElseThrow(() -> ExceptionUtils.notFound("Course", "id", course.getId()));

        // Preserve the creator
        course.setCreatedBy(existingCourse.getCreatedBy());

        if (course.getPrice() < 0) {
            throw ExceptionUtils.badRequest("Course price cannot be negative");
        }

        return courseRepository.save(course);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Course> getCourseById(UUID id) {
        return courseRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Course> getCoursesByCreator(UUID creatorId) {
        User creator = userRepository.findById(creatorId)
                .orElseThrow(() -> ExceptionUtils.notFound("User", "id", creatorId));

        return courseRepository.findByCreatedBy(creator);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Course> getPublishedCourses(Pageable pageable) {
        return courseRepository.findByPublishedTrue(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Course> searchCourses(String keyword, Pageable pageable) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getPublishedCourses(pageable);
        }
        return courseRepository.searchByTitleContaining(keyword, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Course> getCoursesByPriceRange(Integer maxPrice, Pageable pageable) {
        if (maxPrice < 0) {
            throw ExceptionUtils.badRequest("Max price cannot be negative");
        }
        return courseRepository.findByPriceLessThanEqual(maxPrice, pageable);
    }

    @Override
    @Transactional
    public void deleteCourse(UUID id) {
        if (!courseRepository.existsById(id)) {
            throw ExceptionUtils.notFound("Course", "id", id);
        }
        courseRepository.deleteById(id);
    }

    @Override
    @Transactional
    public boolean publishCourse(UUID id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> ExceptionUtils.notFound("Course", "id", id));

        course.setPublished(true);
        courseRepository.save(course);
        return true;
    }

    @Override
    @Transactional
    public boolean unpublishCourse(UUID id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> ExceptionUtils.notFound("Course", "id", id));

        course.setPublished(false);
        courseRepository.save(course);
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Course> getPurchasedCoursesByUser(UUID userId) {
        if (!userRepository.existsById(userId)) {
            throw ExceptionUtils.notFound("User", "id", userId);
        }
        return courseRepository.findPurchasedCoursesByUserId(userId);
    }

    @Override
    public Map<UUID, List<Course>> getCoursesByCreatorIds(List<UUID> userIds) {
        return Map.of();
    }
}