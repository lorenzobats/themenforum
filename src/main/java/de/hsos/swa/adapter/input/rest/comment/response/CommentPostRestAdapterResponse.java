package de.hsos.swa.adapter.input.rest.comment.response;

import de.hsos.swa.application.port.input.commentPost.CommentPostInputPortResponse;

public class CommentPostRestAdapterResponse {
    public String commentId;


    public CommentPostRestAdapterResponse() {
    }

    public CommentPostRestAdapterResponse(String commentId) {
        this.commentId = commentId;
    }

    public static class Converter {
        public static CommentPostRestAdapterResponse fromInputPortResult(CommentPostInputPortResponse inputPortResult) {
            return new CommentPostRestAdapterResponse(inputPortResult.getCommentId().toString());
        }
    }
}
