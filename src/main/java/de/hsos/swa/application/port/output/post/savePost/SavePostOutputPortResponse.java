package de.hsos.swa.application.port.output.post.savePost;
import java.util.UUID;
public class SavePostOutputPortResponse {
    private final UUID postId;

    public SavePostOutputPortResponse(UUID postId) {
        this.postId = postId;
    }

    public UUID getPostId() {
        return postId;
    }
}
