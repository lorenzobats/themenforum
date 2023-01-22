package de.hsos.swa.infrastructure.rest.dto.in;

import de.hsos.swa.application.input.dto.in.CommentPostInputPortRequest;

import javax.validation.constraints.NotBlank;


public class CommentPostRestAdapterRequest {

    @NotBlank(message = "postId is empty")
    public String postId;

    @NotBlank(message = "text is empty")
    public String text;

    public CommentPostRestAdapterRequest() {}

    public static class Converter {
        public static CommentPostInputPortRequest toInputPortCommand(CommentPostRestAdapterRequest adapterRequest, String username) {
            return new CommentPostInputPortRequest(adapterRequest.postId, username, adapterRequest.text);
        }
    }
}
