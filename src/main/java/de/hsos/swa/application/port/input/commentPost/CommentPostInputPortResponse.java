package de.hsos.swa.application.port.input.commentPost;
import java.util.UUID;
public class CommentPostInputPortResponse {
    private final UUID commentId;

    public CommentPostInputPortResponse(UUID commentId) {
        this.commentId = commentId;
    }

    public UUID getCommentId() {
        return commentId;
    }
}
