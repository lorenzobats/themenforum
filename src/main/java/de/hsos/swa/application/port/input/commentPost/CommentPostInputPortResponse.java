package de.hsos.swa.application.port.input.commentPost;
import java.util.UUID;

public class CommentPostInputPortResponse {
    private final UUID postId;

    public CommentPostInputPortResponse(UUID postId) {
        this.postId = postId;
    }

    public UUID getPostId() {
        return postId;
    }
}
