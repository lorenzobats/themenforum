package de.hsos.swa.infrastructure.rest.request;

import de.hsos.swa.application.input.request.ReplyToCommentInputPortRequest;


public class ReplyToCommentRestAdapterRequest {

    public String postId;

    public String text;

    public ReplyToCommentRestAdapterRequest() {}

    public static class Converter {
        public static ReplyToCommentInputPortRequest toInputPort(ReplyToCommentRestAdapterRequest adapterRequest, String commentId, String username) {
            return new ReplyToCommentInputPortRequest(adapterRequest.postId, commentId, username, adapterRequest.text);
        }
    }
}
