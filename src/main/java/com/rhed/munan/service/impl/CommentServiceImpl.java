package com.rhed.munan.service.impl;

import com.rhed.munan.exception.ExceptionUtils;
import com.rhed.munan.model.Blog;
import com.rhed.munan.model.Comment;
import com.rhed.munan.model.User;
import com.rhed.munan.repository.BlogRepository;
import com.rhed.munan.repository.CommentRepository;
import com.rhed.munan.repository.UserRepository;
import com.rhed.munan.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final BlogRepository blogRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public Comment createComment(String content, UUID blogId, UUID userId, UUID parentCommentId) {
        if (content == null || content.trim().isEmpty()) {
            throw ExceptionUtils.badRequest("Comment content cannot be empty");
        }

        Blog blog = blogRepository.findById(blogId)
                .orElseThrow(() -> ExceptionUtils.notFound("Blog", "id", blogId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> ExceptionUtils.notFound("User", "id", userId));

        Comment comment = new Comment();
        comment.setBlog(blog);
        comment.setUser(user);
        comment.setContent(content);
        comment.setCreatedAt(LocalDateTime.now());

        // Set parent comment if provided
        if (parentCommentId != null) {
            Comment parentComment = commentRepository.findById(parentCommentId)
                    .orElseThrow(() -> ExceptionUtils.notFound("Comment", "id", parentCommentId));

            // Ensure parent comment belongs to the same blog
            if (!parentComment.getBlog().getId().equals(blogId)) {
                throw ExceptionUtils.badRequest("Parent comment does not belong to the specified blog");
            }

            // Ensure we're not creating a nested reply (only one level of nesting)
            if (parentComment.getParent() != null) {
                throw ExceptionUtils.badRequest("Cannot reply to a reply. Only one level of nesting is allowed.");
            }

            comment.setParent(parentComment);
        }

        return commentRepository.save(comment);
    }

    @Override
    @Transactional
    public Comment updateComment(UUID commentId, String content) {
        if (content == null || content.trim().isEmpty()) {
            throw ExceptionUtils.badRequest("Comment content cannot be empty");
        }

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> ExceptionUtils.notFound("Comment", "id", commentId));

        comment.setContent(content);
        return commentRepository.save(comment);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Comment> getCommentById(UUID id) {
        return commentRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Comment> getRootCommentsByBlog(UUID blogId) {
        if (!blogRepository.existsById(blogId)) {
            throw ExceptionUtils.notFound("Blog", "id", blogId);
        }
        return commentRepository.findRootCommentsByBlogId(blogId);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Comment> getRootCommentsByBlog(UUID blogId, Pageable pageable) {
        if (!blogRepository.existsById(blogId)) {
            throw ExceptionUtils.notFound("Blog", "id", blogId);
        }
        return commentRepository.findRootCommentsByBlogId(blogId, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Comment> getRepliesByComment(UUID commentId) {
        Comment parentComment = commentRepository.findById(commentId)
                .orElseThrow(() -> ExceptionUtils.notFound("Comment", "id", commentId));

        return commentRepository.findByParent(parentComment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Comment> getCommentsByUser(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> ExceptionUtils.notFound("User", "id", userId));

        return commentRepository.findByUser(user);
    }

    @Override
    @Transactional
    public void deleteComment(UUID id) {
        if (!commentRepository.existsById(id)) {
            throw ExceptionUtils.notFound("Comment", "id", id);
        }
        commentRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Long countCommentsByBlog(UUID blogId) {
        if (!blogRepository.existsById(blogId)) {
            throw ExceptionUtils.notFound("Blog", "id", blogId);
        }
        return commentRepository.countCommentsByBlogId(blogId);
    }
}