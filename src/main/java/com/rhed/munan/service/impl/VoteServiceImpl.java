package com.rhed.munan.service.impl;

import com.rhed.munan.exception.ExceptionUtils;
import com.rhed.munan.model.Blog;
import com.rhed.munan.model.User;
import com.rhed.munan.model.Vote;
import com.rhed.munan.model.Vote.VoteType;
import com.rhed.munan.repository.BlogRepository;
import com.rhed.munan.repository.UserRepository;
import com.rhed.munan.repository.VoteRepository;
import com.rhed.munan.service.VoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VoteServiceImpl implements VoteService {

    private final VoteRepository voteRepository;
    private final BlogRepository blogRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public Vote voteOnBlog(UUID blogId, UUID userId, VoteType voteType) {
        Blog blog = blogRepository.findById(blogId)
                .orElseThrow(() -> ExceptionUtils.notFound("Blog", "id", blogId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> ExceptionUtils.notFound("User", "id", userId));

        // Check if user already voted on this blog
        Optional<Vote> existingVote = voteRepository.findByBlogAndUser(blog, user);

        if (existingVote.isPresent()) {
            Vote vote = existingVote.get();

            // If same vote type, remove the vote (toggle)
            if (vote.getVoteType() == voteType) {
                voteRepository.delete(vote);
                updateBlogVoteCount(blogId);
                return vote;
            } else {
                // Change vote type
                vote.setVoteType(voteType);
                Vote savedVote = voteRepository.save(vote);
                updateBlogVoteCount(blogId);
                return savedVote;
            }
        } else {
            // Create new vote
            Vote vote = new Vote();
            vote.setBlog(blog);
            vote.setUser(user);
            vote.setVoteType(voteType);
            Vote savedVote = voteRepository.save(vote);
            updateBlogVoteCount(blogId);
            return savedVote;
        }
    }

    @Override
    @Transactional
    public void removeVote(UUID blogId, UUID userId) {
        Blog blog = blogRepository.findById(blogId)
                .orElseThrow(() -> ExceptionUtils.notFound("Blog", "id", blogId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> ExceptionUtils.notFound("User", "id", userId));

        voteRepository.deleteByBlogAndUser(blog, user);
        updateBlogVoteCount(blogId);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Vote> getVoteByBlogAndUser(UUID blogId, UUID userId) {
        Blog blog = blogRepository.findById(blogId)
                .orElseThrow(() -> ExceptionUtils.notFound("Blog", "id", blogId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> ExceptionUtils.notFound("User", "id", userId));

        return voteRepository.findByBlogAndUser(blog, user);
    }

    @Override
    @Transactional(readOnly = true)
    public Long countUpVotesByBlog(UUID blogId) {
        if (!blogRepository.existsById(blogId)) {
            throw ExceptionUtils.notFound("Blog", "id", blogId);
        }
        return voteRepository.countByBlogIdAndVoteType(blogId, VoteType.UP);
    }

    @Override
    @Transactional(readOnly = true)
    public Long countDownVotesByBlog(UUID blogId) {
        if (!blogRepository.existsById(blogId)) {
            throw ExceptionUtils.notFound("Blog", "id", blogId);
        }
        return voteRepository.countByBlogIdAndVoteType(blogId, VoteType.DOWN);
    }

    @Override
    @Transactional(readOnly = true)
    public Long calculateNetVotes(UUID blogId) {
        Long upVotes = countUpVotesByBlog(blogId);
        Long downVotes = countDownVotesByBlog(blogId);
        return upVotes - downVotes;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasUserVotedOnBlog(UUID blogId, UUID userId) {
        Blog blog = blogRepository.findById(blogId)
                .orElseThrow(() -> ExceptionUtils.notFound("Blog", "id", blogId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> ExceptionUtils.notFound("User", "id", userId));

        return voteRepository.existsByBlogAndUser(blog, user);
    }

    private void updateBlogVoteCount(UUID blogId) {
        Long netVotes = calculateNetVotes(blogId);
        blogRepository.updateVoteCount(blogId, netVotes.intValue());
    }
}