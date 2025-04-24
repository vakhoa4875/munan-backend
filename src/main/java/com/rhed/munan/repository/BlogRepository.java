package com.rhed.munan.repository;

import com.rhed.munan.model.Blog;
import com.rhed.munan.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BlogRepository extends JpaRepository<Blog, UUID> {

    Optional<Blog> findBySlug(String slug);

    Page<Blog> findByStatus(String status, Pageable pageable);

    List<Blog> findByCreatedBy(User author);

    @Query("SELECT b FROM Blog b WHERE b.status = 'published' AND LOWER(b.title) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Blog> searchByTitleContaining(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT b FROM Blog b WHERE b.status = 'published' AND b.category = :category")
    Page<Blog> findByCategory(@Param("category") String category, Pageable pageable);

    @Query(value = "SELECT * FROM blogs b WHERE b.status = 'published' AND :tag = ANY(b.tags)",
            nativeQuery = true,
            countQuery = "SELECT COUNT(*) FROM blogs b WHERE b.status = 'published' AND :tag = ANY(b.tags)")
    Page<Blog> findByTag(@Param("tag") String tag, Pageable pageable);

    @Modifying
    @Query("UPDATE Blog b SET b.views = b.views + 1 WHERE b.id = :id")
    void incrementViews(@Param("id") UUID id);

    @Modifying
    @Query("UPDATE Blog b SET b.votes = :voteCount WHERE b.id = :id")
    void updateVoteCount(@Param("id") UUID id, @Param("voteCount") int voteCount);

    @Query("SELECT b FROM Blog b WHERE b.status = 'published' ORDER BY b.views DESC")
    Page<Blog> findMostViewed(Pageable pageable);

    @Query("SELECT b FROM Blog b WHERE b.status = 'published' ORDER BY b.votes DESC")
    Page<Blog> findMostVoted(Pageable pageable);
}