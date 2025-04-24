package com.rhed.munan.service;

import com.rhed.munan.model.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface CourseService {

    Course createCourse(Course course, UUID creatorId);

    Course updateCourse(Course course);

    Optional<Course> getCourseById(UUID id);

    List<Course> getCoursesByCreator(UUID creatorId);

    Page<Course> getPublishedCourses(Pageable pageable);

    Page<Course> searchCourses(String keyword, Pageable pageable);

    Page<Course> getCoursesByPriceRange(Integer maxPrice, Pageable pageable);

    void deleteCourse(UUID id);

    boolean publishCourse(UUID id);

    boolean unpublishCourse(UUID id);

    List<Course> getPurchasedCoursesByUser(UUID userId);

    Map<UUID, List<Course>> getCoursesByCreatorIds(List<UUID> userIds);
}