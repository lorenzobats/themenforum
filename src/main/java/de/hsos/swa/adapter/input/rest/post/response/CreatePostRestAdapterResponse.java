package de.hsos.swa.adapter.input.rest.post.response;


import java.util.UUID;

public class CreatePostRestAdapterResponse {

    public String id;

    public CreatePostRestAdapterResponse() {
    }

    public CreatePostRestAdapterResponse(String id) {
        this.id = id;
    }

    // TODO: Als link zurückgeben - dann brauchen wir die Response nicht mehr
    public static class Converter {
        public static CreatePostRestAdapterResponse fromInputPortResult(UUID response) {
            return new CreatePostRestAdapterResponse(response.toString());
        }
    }
}
