package de.hsos.swa.application.port.input.commentPost;

import de.hsos.swa.application.port.input._shared.SelfValidating;

import javax.validation.constraints.NotEmpty;

public class CommentPostInputPortRequest extends SelfValidating<CommentPostInputPortRequest> {

    @NotEmpty(message="Field: 'postId' is missing")
    private final String postId;

    @NotEmpty(message="Field: 'userId' is missing")
    private final String userId;

    @NotEmpty(message = "Field: 'commentText' is missing")
    private final String commentText;

    public CommentPostInputPortRequest(String postId, String userId, String commentText) {
        this.postId = postId;
        this.userId = userId;
        this.commentText = commentText;
    }

    public String getPostId() {
        return postId;
    }

    public String getUserId() {
        return userId;
    }

    public String getCommentText() {
        return commentText;
    }
}
