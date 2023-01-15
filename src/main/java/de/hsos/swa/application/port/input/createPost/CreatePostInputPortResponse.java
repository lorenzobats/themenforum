package de.hsos.swa.application.port.input.createPost;
import java.util.UUID;
public class CreatePostInputPortResponse {
    private final UUID postId;

    public CreatePostInputPortResponse(UUID postId) {
        this.postId = postId;
    }

    public UUID getPostId() {
        return postId;
    }
}
