package de.hsos.swa.adapter.input.rest.comment.response;

import java.util.UUID;

public class ReplyToCommentRestAdapterResponse {
    public String commentId;


    public ReplyToCommentRestAdapterResponse() {
    }

    public ReplyToCommentRestAdapterResponse(String commentId) {
        this.commentId = commentId;
    }

    public static class Converter {
        public static ReplyToCommentRestAdapterResponse fromInputPortResult(UUID result) {
            return new ReplyToCommentRestAdapterResponse(result.toString());
        }
    }
}
