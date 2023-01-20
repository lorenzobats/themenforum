package de.hsos.swa.application.port.input.replyToComment;

import de.hsos.swa.application.port.input._shared.SelfValidating;
import de.hsos.swa.application.port.input.registerUser.RegisterUserInputPortRequest;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class ReplyToCommentInputPortRequest extends SelfValidating<RegisterUserInputPortRequest> {
    @NotEmpty(message="postId is missing")
    @Pattern(regexp = "[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}", message = "postId is not valid")
    private final String postId;

    @NotEmpty(message="commentId is missing")
    @Pattern(regexp = "[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}", message = "commentId is not valid")
    private final String commentId;

    @NotEmpty(message="username is missing")
    @Size(min = 4, max = 20, message = "username must be between 4 and 20 characters")
    @Pattern(regexp = "^[a-zA-Z0-9_.-]*$", message = "username format is not valid")
    private final String username;

    @NotEmpty(message = "commentText is missing")
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
        this.validateSelf();
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
