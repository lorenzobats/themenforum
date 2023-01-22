package de.hsos.swa.application.input.dto.in;

import de.hsos.swa.domain.vo.VoteType;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class VotePostInputPortRequest {

    @NotEmpty(message="postId is missing")
    private final String postId;

    @NotEmpty(message="username is missing")
    private final String username;

    @NotNull(message="voteType is missing")
    private final VoteType voteType;

    public VotePostInputPortRequest(String postId, String username, VoteType voteType) {
        this.postId = postId;
        this.username = username;
        this.voteType = voteType;
    }

    public String getPostId() {
        return postId;
    }

    public String getUsername() {
        return username;
    }

    public VoteType getVoteType() {
        return voteType;
    }
}
