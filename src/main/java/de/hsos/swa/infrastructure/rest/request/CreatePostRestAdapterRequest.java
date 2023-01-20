package de.hsos.swa.infrastructure.rest.request;

import de.hsos.swa.application.input.request.CreatePostInputPortRequest;

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
