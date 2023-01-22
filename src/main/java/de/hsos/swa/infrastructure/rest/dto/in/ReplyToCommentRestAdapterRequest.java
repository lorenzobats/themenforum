package de.hsos.swa.infrastructure.rest.dto.in;

import de.hsos.swa.application.input.dto.in.ReplyToCommentInputPortRequest;

import javax.validation.constraints.NotBlank;


public class ReplyToCommentRestAdapterRequest {

    @NotBlank(message = "postId is empty")
    public String postId;

    @NotBlank(message = "text is empty")
    public String text;

    public ReplyToCommentRestAdapterRequest() {}

    public static class Converter {
        public static ReplyToCommentInputPortRequest toInputPortCommand(ReplyToCommentRestAdapterRequest adapterRequest, String commentId, String username) {
            return new ReplyToCommentInputPortRequest(adapterRequest.postId, commentId, username, adapterRequest.text);
        }
    }
}
