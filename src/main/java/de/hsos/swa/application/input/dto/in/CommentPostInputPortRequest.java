package de.hsos.swa.application.input.dto.in;

import javax.validation.constraints.NotEmpty;

public class CommentPostInputPortRequest  {

    @NotEmpty(message="postId is missing")
    private final String postId;

    @NotEmpty(message="userId is missing")
    private final String username;

    @NotEmpty(message = "Text is missing")
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
