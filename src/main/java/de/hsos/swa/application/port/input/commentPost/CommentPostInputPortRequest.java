package de.hsos.swa.application.port.input.commentPost;

import de.hsos.swa.application.port.input._shared.SelfValidating;

import javax.validation.constraints.NotEmpty;

public class CommentPostInputPortRequest extends SelfValidating<CommentPostInputPortRequest> {

    @NotEmpty(message="Field: 'postId' is missing")
    private final String postId;

    @NotEmpty(message="Field: 'userId' is missing")
    private final String username;

    @NotEmpty(message = "Field: 'commentText' is missing")
    private final String commentText;

    public CommentPostInputPortRequest(String postId, String username, String commentText) {
        this.postId = postId;
        this.username = username;
        this.commentText = commentText;
    }

    public String getPostId() {
        return postId;
    }

    public String getUsername() {
        return username;
    }

    public String getCommentText() {
        return commentText;
    }
}
