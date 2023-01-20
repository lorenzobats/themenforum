package de.hsos.swa.application.port.input.replyToComment;

import java.util.UUID;

public class ReplyToCommentInputPortResponse {
    private final UUID commentId;

    public ReplyToCommentInputPortResponse(UUID commentId) {
        this.commentId = commentId;
    }

    public UUID getCommentId() {
        return commentId;
    }
}
