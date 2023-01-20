package de.hsos.swa.adapter.input.rest.post.request;

import de.hsos.swa.application.port.input.request.CreatePostInputPortRequest;

import javax.validation.constraints.NotBlank;


public class CreatePostRestAdapterRequest {

    @NotBlank(message = "Post Titel darf nicht leer sein")
    public String title;

    public CreatePostRestAdapterRequest() {}

    public static class Converter {
        public static CreatePostInputPortRequest toInputPortCommand(CreatePostRestAdapterRequest adapterRequest, String username) {
            return new CreatePostInputPortRequest(adapterRequest.title, username);
        }
    }
}
