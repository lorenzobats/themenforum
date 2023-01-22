package de.hsos.swa.infrastructure.rest.dto.in;

import de.hsos.swa.application.input.dto.in.ReplyToCommentInputPortRequest;


public class ReplyToCommentRestAdapterRequest {

    public String postId;

    public String text;

    public ReplyToCommentRestAdapterRequest() {}

    public static class Converter {
        public static ReplyToCommentInputPortRequest toInputPortCommand(ReplyToCommentRestAdapterRequest adapterRequest, String commentId, String username) {
            return new ReplyToCommentInputPortRequest(adapterRequest.postId, commentId, username, adapterRequest.text);
        }
    }
}
