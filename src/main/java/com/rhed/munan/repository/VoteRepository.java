package com.rhed.munan.repository;

import com.rhed.munan.model.Blog;
import com.rhed.munan.model.User;
import com.rhed.munan.model.Vote;
import com.rhed.munan.model.Vote.VoteType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {

    Optional<Vote> findByBlogAndUser(Blog blog, User user);

    boolean existsByBlogAndUser(Blog blog, User user);

    void deleteByBlogAndUser(Blog blog, User user);

    @Query("SELECT COUNT(v) FROM Vote v WHERE v.blog.id = :blogId AND v.voteType = :voteType")
    Long countByBlogIdAndVoteType(@Param("blogId") UUID blogId, @Param("voteType") VoteType voteType);
}