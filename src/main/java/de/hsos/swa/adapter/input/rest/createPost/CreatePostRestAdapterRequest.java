package de.hsos.swa.adapter.input.rest.createPost;

import de.hsos.swa.application.port.input.createPost.CreatePostInputPortRequest;
import de.hsos.swa.application.port.input.registerUser.RegisterUserInputPortRequest;

public class CreatePostRestAdapterRequest {

    public String title;

    public CreatePostRestAdapterRequest() {
    }

    public CreatePostRestAdapterRequest(String title) {
        this.title = title;
    }

    public static class Converter {
        public static CreatePostInputPortRequest toInputPortRequest(CreatePostRestAdapterRequest request, String username) {
            return new CreatePostInputPortRequest(request.title, username);
        }
    }
}
