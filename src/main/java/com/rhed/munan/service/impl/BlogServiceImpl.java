package com.rhed.munan.service.impl;

import com.rhed.munan.exception.ExceptionUtils;
import com.rhed.munan.model.Blog;
import com.rhed.munan.model.User;
import com.rhed.munan.repository.BlogRepository;
import com.rhed.munan.repository.UserRepository;
import com.rhed.munan.service.BlogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class BlogServiceImpl implements BlogService {
    private final BlogRepository blogRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public Blog createBlog(Blog blog, UUID authorId) {
        User author = userRepository.findById(authorId)
                .orElseThrow(() -> ExceptionUtils.notFound("User", "id", authorId));

        blog.setCreatedBy(author);

        // Generate slug if not provided
        if (blog.getSlug() == null || blog.getSlug().trim().isEmpty()) {
            blog.setSlug(generateSlug(blog.getTitle()));
        } else {
            // Validate and normalize slug
            blog.setSlug(normalizeSlug(blog.getSlug()));
        }

        // Check if slug already exists
        if (blogRepository.findBySlug(blog.getSlug()).isPresent()) {
            throw ExceptionUtils.duplicate("Blog", "slug", blog.getSlug());
        }

        // Set default status if not provided
        if (blog.getStatus() == null || blog.getStatus().trim().isEmpty()) {
            blog.setStatus("draft");
        }

        blog.setCreatedAt(LocalDateTime.now());

        return blogRepository.save(blog);
    }

    @Override
    @Transactional
    public Blog updateBlog(Blog blog) {
        Blog existingBlog = blogRepository.findById(blog.getId())
                .orElseThrow(() -> ExceptionUtils.notFound("Blog", "id", blog.getId()));

        // Preserve the author
        blog.setCreatedBy(existingBlog.getCreatedBy());
        blog.setCreatedAt(existingBlog.getCreatedAt());

        // Update slug if changed
        if (blog.getSlug() == null || blog.getSlug().trim().isEmpty()) {
            blog.setSlug(generateSlug(blog.getTitle()));
        } else {
            blog.setSlug(normalizeSlug(blog.getSlug()));
        }

        // Check if new slug conflicts with existing blogs (excluding current blog)
        Optional<Blog> blogWithSameSlug = blogRepository.findBySlug(blog.getSlug());
        if (blogWithSameSlug.isPresent() && !blogWithSameSlug.get().getId().equals(blog.getId())) {
            throw ExceptionUtils.duplicate("Blog", "slug", blog.getSlug());
        }

        blog.setUpdatedAt(LocalDateTime.now());

        return blogRepository.save(blog);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Blog> getBlogById(UUID id) {
        return blogRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Blog> getBlogBySlug(String slug) {
        return blogRepository.findBySlug(slug);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Blog> getPublishedBlogs(Pageable pageable) {
        return blogRepository.findByStatus("published", pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Blog> getBlogsByAuthor(UUID authorId) {
        User author = userRepository.findById(authorId)
                .orElseThrow(() -> ExceptionUtils.notFound("User", "id", authorId));

        return blogRepository.findByCreatedBy(author);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Blog> searchBlogs(String keyword, Pageable pageable) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getPublishedBlogs(pageable);
        }
        return blogRepository.searchByTitleContaining(keyword, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Blog> getBlogsByCategory(String category, Pageable pageable) {
        if (category == null || category.trim().isEmpty()) {
            throw ExceptionUtils.badRequest("Category cannot be empty");
        }
        return blogRepository.findByCategory(category, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Blog> getBlogsByTag(String tag, Pageable pageable) {
        if (tag == null || tag.trim().isEmpty()) {
            throw ExceptionUtils.badRequest("Tag cannot be empty");
        }
        return blogRepository.findByTag(tag, pageable);
    }

    @Override
    @Transactional
    public void deleteBlog(UUID id) {
        if (!blogRepository.existsById(id)) {
            throw ExceptionUtils.notFound("Blog", "id", id);
        }
        blogRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void incrementBlogViews(UUID id) {
        if (!blogRepository.existsById(id)) {
            throw ExceptionUtils.notFound("Blog", "id", id);
        }
        blogRepository.incrementViews(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Blog> getMostViewedBlogs(Pageable pageable) {
        return blogRepository.findMostViewed(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Blog> getMostVotedBlogs(Pageable pageable) {
        return blogRepository.findMostVoted(pageable);
    }

    @Override
    public String generateSlug(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw ExceptionUtils.badRequest("Title cannot be empty for slug generation");
        }

        // Convert to lowercase
        String slug = title.toLowerCase();

        // Replace special characters with hyphens
        slug = slug.replaceAll("[^a-z0-9\\s-]", "");

        // Replace spaces with hyphens
        slug = slug.replaceAll("\\s+", "-");

        // Remove consecutive hyphens
        slug = slug.replaceAll("-+", "-");

        // Trim hyphens from start and end
        slug = slug.replaceAll("^-|-$", "");

        // Ensure uniqueness by adding timestamp if needed
        Optional<Blog> existingBlog = blogRepository.findBySlug(slug);
        if (existingBlog.isPresent()) {
            slug = slug + "-" + System.currentTimeMillis() % 10000;
        }

        return slug;
    }

    private String normalizeSlug(String slug) {
        if (slug == null || slug.trim().isEmpty()) {
            throw ExceptionUtils.badRequest("Slug cannot be empty");
        }

        // Convert to lowercase
        slug = slug.toLowerCase();

        // Replace special characters with hyphens
        slug = slug.replaceAll("[^a-z0-9\\s-]", "");

        // Replace spaces with hyphens
        slug = slug.replaceAll("\\s+", "-");

        // Remove consecutive hyphens
        slug = slug.replaceAll("-+", "-");

        // Trim hyphens from start and end
        slug = slug.replaceAll("^-|-$", "");

        if (slug.isEmpty()) {
            throw ExceptionUtils.badRequest("Slug contains only invalid characters");
        }

        return slug;
    }
}