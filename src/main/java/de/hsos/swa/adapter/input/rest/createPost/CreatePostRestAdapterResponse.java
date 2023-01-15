package de.hsos.swa.adapter.input.rest.createPost;

import de.hsos.swa.application.port.input.createPost.CreatePostInputPortResponse;
import de.hsos.swa.application.port.input.registerUser.RegisterUserInputPortResponse;

public class CreatePostRestAdapterResponse {

    public String id;

    public CreatePostRestAdapterResponse() {
    }

    public CreatePostRestAdapterResponse(String id) {
        this.id = id;
    }

    // TODO: Als link zurückgeben - dann brauchen wir die Response nicht mehr
    public static class Converter {
        public static CreatePostRestAdapterResponse fromInputPortResult(CreatePostInputPortResponse response) {
            return new CreatePostRestAdapterResponse(response.getPostId().toString());
        }
    }
}
