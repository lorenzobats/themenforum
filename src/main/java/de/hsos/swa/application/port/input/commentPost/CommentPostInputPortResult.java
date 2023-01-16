package de.hsos.swa.application.port.input.commentPost;
import java.util.UUID;
public class CommentPostInputPortResult {
    private final UUID commentId;

    public CommentPostInputPortResult(UUID commentId) {
        this.commentId = commentId;
    }

    public UUID getCommentId() {
        return commentId;
    }
}
