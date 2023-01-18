package de.hsos.swa.application.port.input.replyToComment;

import java.util.UUID;

public class ReplyToCommentInputPortResponse {
    private final UUID postId;

    public ReplyToCommentInputPortResponse(UUID postId) {
        this.postId = postId;

    }

    public UUID getPostId() {
        return postId;
    }
}
