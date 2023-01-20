package de.hsos.swa.adapter.input.rest.comment.response;


import java.util.UUID;

public class CommentPostRestAdapterResponse {
    public String commentId;


    public CommentPostRestAdapterResponse() {
    }

    public CommentPostRestAdapterResponse(String commentId) {
        this.commentId = commentId;
    }

    public static class Converter {
        public static CommentPostRestAdapterResponse fromInputPortResult(UUID result) {
            return new CommentPostRestAdapterResponse(result.toString());
        }
    }
}
