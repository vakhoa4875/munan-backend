package com.rhed.munan.service;

import com.rhed.munan.model.Vote;
import com.rhed.munan.model.Vote.VoteType;

import java.util.Optional;
import java.util.UUID;

public interface VoteService {

    Vote voteOnBlog(UUID blogId, UUID userId, VoteType voteType);

    void removeVote(UUID blogId, UUID userId);

    Optional<Vote> getVoteByBlogAndUser(UUID blogId, UUID userId);

    Long countUpVotesByBlog(UUID blogId);

    Long countDownVotesByBlog(UUID blogId);

    Long calculateNetVotes(UUID blogId);

    boolean hasUserVotedOnBlog(UUID blogId, UUID userId);
}