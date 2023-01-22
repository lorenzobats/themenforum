package de.hsos.swa.application.input.dto.in;

import de.hsos.swa.domain.vo.VoteType;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class VoteCommentInputPortRequest {

    @NotEmpty(message="commentId is missing")
    private final String commentId;

    @NotEmpty(message="postId is missing")
    private final String postId;

    @NotEmpty(message="username is missing")
    private final String username;

    @NotNull(message="voteType is missing")
    private final VoteType voteType;

    public VoteCommentInputPortRequest(String commentId, String postId, String username, VoteType voteType) {
        this.commentId = commentId;
        this.postId = postId;
        this.username = username;
        this.voteType = voteType;
    }

    public String getCommentId() {
        return commentId;
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
