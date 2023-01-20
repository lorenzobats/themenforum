package de.hsos.swa.adapter.input.rest.post.request;

import de.hsos.swa.application.port.input.createPost.CreatePostInputPortRequest;


public class CreatePostRestAdapterRequest {

    public String title;

    public CreatePostRestAdapterRequest() {}

    public static class Converter {
        public static CreatePostInputPortRequest toInputPortCommand(CreatePostRestAdapterRequest adapterRequest, String username) {
            return new CreatePostInputPortRequest(adapterRequest.title, username);
        }
    }
}
