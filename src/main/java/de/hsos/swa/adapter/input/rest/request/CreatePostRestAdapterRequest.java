package de.hsos.swa.adapter.input.rest.request;

import de.hsos.swa.application.port.input.createPost.CreatePostInputPortRequest;


public class CreatePostRestAdapterRequest {

    public String title;

    public CreatePostRestAdapterRequest() {}

    public static class Converter {
        public static CreatePostInputPortRequest toInputPortRequest(CreatePostRestAdapterRequest request, String username) {
            return new CreatePostInputPortRequest(request.title, username);
        }
    }
}
