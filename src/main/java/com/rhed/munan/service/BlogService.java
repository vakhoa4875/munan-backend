package com.rhed.munan.service;

import com.rhed.munan.model.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BlogService {

    Blog createBlog(Blog blog, UUID authorId);

    Blog updateBlog(Blog blog);

    Optional<Blog> getBlogById(UUID id);

    Optional<Blog> getBlogBySlug(String slug);

    Page<Blog> getPublishedBlogs(Pageable pageable);

    List<Blog> getBlogsByAuthor(UUID authorId);

    Page<Blog> searchBlogs(String keyword, Pageable pageable);

    Page<Blog> getBlogsByCategory(String category, Pageable pageable);

    Page<Blog> getBlogsByTag(String tag, Pageable pageable);

    void deleteBlog(UUID id);

    void incrementBlogViews(UUID id);

    Page<Blog> getMostViewedBlogs(Pageable pageable);

    Page<Blog> getMostVotedBlogs(Pageable pageable);

    String generateSlug(String title);
}