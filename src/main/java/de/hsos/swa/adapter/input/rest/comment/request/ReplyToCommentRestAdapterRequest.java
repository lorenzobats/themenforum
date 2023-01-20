package de.hsos.swa.adapter.input.rest.comment.request;

import de.hsos.swa.application.port.input.request.ReplyToCommentInputPortRequest;


public class ReplyToCommentRestAdapterRequest {

    public String postId;

    public String commentId;

    public String text;

    public ReplyToCommentRestAdapterRequest() {}

    public static class Converter {
        public static ReplyToCommentInputPortRequest toInputPortRequest(ReplyToCommentRestAdapterRequest adapterRequest, String username) {
            return new ReplyToCommentInputPortRequest(adapterRequest.postId, adapterRequest.commentId, adapterRequest.text, username);
        }
    }
}
