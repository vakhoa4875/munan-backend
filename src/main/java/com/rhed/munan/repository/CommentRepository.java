package com.rhed.munan.repository;

import com.rhed.munan.model.Comment;
import com.rhed.munan.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CommentRepository extends JpaRepository<Comment, UUID> {

    List<Comment> findByUser(User user);

    List<Comment> findByParent(Comment parentComment);

    @Query("SELECT c FROM Comment c WHERE c.blog.id = :blogId AND c.parent IS NULL ORDER BY c.createdAt DESC")
    List<Comment> findRootCommentsByBlogId(@Param("blogId") UUID blogId);

    @Query("SELECT c FROM Comment c WHERE c.blog.id = :blogId AND c.parent IS NULL ORDER BY c.createdAt DESC")
    Page<Comment> findRootCommentsByBlogId(@Param("blogId") UUID blogId, Pageable pageable);

    @Query("SELECT COUNT(c) FROM Comment c WHERE c.blog.id = :blogId")
    Long countCommentsByBlogId(@Param("blogId") UUID blogId);
}