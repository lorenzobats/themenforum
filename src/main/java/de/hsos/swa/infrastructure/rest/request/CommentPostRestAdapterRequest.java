package de.hsos.swa.infrastructure.rest.request;

import de.hsos.swa.application.input.request.CommentPostInputPortRequest;


public class CommentPostRestAdapterRequest {

    public String postId;

    public String text;

    public CommentPostRestAdapterRequest() {}

    public static class Converter {
        public static CommentPostInputPortRequest toInputPort(CommentPostRestAdapterRequest adapterRequest, String username) {
            return new CommentPostInputPortRequest(adapterRequest.postId, username, adapterRequest.text);
        }
    }
}
