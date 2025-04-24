package com.rhed.munan.service;

import com.rhed.munan.model.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CommentService {

    Comment createComment(String content, UUID blogId, UUID userId, UUID parentCommentId);

    Comment updateComment(UUID commentId, String content);

    Optional<Comment> getCommentById(UUID id);

    List<Comment> getRootCommentsByBlog(UUID blogId);

    Page<Comment> getRootCommentsByBlog(UUID blogId, Pageable pageable);

    List<Comment> getRepliesByComment(UUID commentId);

    List<Comment> getCommentsByUser(UUID userId);

    void deleteComment(UUID id);

    Long countCommentsByBlog(UUID blogId);
}