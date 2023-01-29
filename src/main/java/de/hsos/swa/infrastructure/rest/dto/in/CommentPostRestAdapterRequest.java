package de.hsos.swa.infrastructure.rest.dto.in;

import de.hsos.swa.application.input.dto.in.CommentPostInputPortRequest;

public class CommentPostRestAdapterRequest {

    public String postId;

    public String text;

    public static class Converter {
        public static CommentPostInputPortRequest toInputPortCommand(CommentPostRestAdapterRequest adapterRequest, String username) {
            return new CommentPostInputPortRequest(adapterRequest.postId, username, adapterRequest.text);
        }
    }
}
