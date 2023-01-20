package de.hsos.swa.adapter.input.rest.comment.response;

import de.hsos.swa.application.port.input.replyToComment.ReplyToCommentInputPortResponse;

public class ReplyToCommentRestAdapterResponse {
    public String commentId;


    public ReplyToCommentRestAdapterResponse() {
    }

    public ReplyToCommentRestAdapterResponse(String commentId) {
        this.commentId = commentId;
    }

    public static class Converter {
        public static ReplyToCommentRestAdapterResponse fromInputPortResult(ReplyToCommentInputPortResponse inputPortResult) {
            return new ReplyToCommentRestAdapterResponse(inputPortResult.getCommentId().toString());
        }
    }
}
