package de.hsos.swa.application.port.output.post.saveComment;
import java.util.UUID;
public class SaveCommentOutputPortResponse {
    private final UUID postId;

    public SaveCommentOutputPortResponse(UUID postId) {
        this.postId = postId;
    }

    public UUID getPostId() {
        return postId;
    }
}
