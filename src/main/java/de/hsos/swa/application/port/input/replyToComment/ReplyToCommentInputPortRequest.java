package de.hsos.swa.application.port.input.replyToComment;

import javax.validation.constraints.NotEmpty;

public class ReplyToCommentInputPortRequest {
    @NotEmpty(message="Field: 'postId' is missing")
    private final String postId;

    @NotEmpty(message="Field: 'postId' is missing")
    private final String commentId;

    @NotEmpty(message="Field: 'userId' is missing")
    private final String username;

    @NotEmpty(message = "Field: 'commentText' is missing")
    private final String commentText;


    public ReplyToCommentInputPortRequest(
            String postId,
            String commentId,
            String username,
            String commentText) {
        this.postId = postId;
        this.commentId = commentId;
        this.username = username;
        this.commentText = commentText;
    }

    public String getPostId() {
        return postId;
    }

    public String getCommentId() {
        return commentId;
    }

    public String getUsername() {
        return username;
    }

    public String getCommentText() {
        return commentText;
    }
}
