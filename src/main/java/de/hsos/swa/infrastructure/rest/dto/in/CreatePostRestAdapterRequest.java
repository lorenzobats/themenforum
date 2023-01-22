package de.hsos.swa.infrastructure.rest.dto.in;

import de.hsos.swa.application.input.dto.in.CreatePostInputPortRequest;

import javax.validation.constraints.NotBlank;
import java.util.UUID;


public class CreatePostRestAdapterRequest {

    @NotBlank(message = "Post Titel darf nicht leer sein")
    public String title;

    @NotBlank(message = "Post Content darf nicht leer sein")
    public String content;

    @NotBlank(message = "Post TopicId darf nicht leer sein")
    public String topicId;

    public CreatePostRestAdapterRequest() {}

    public static class Converter {
        public static CreatePostInputPortRequest toInputPort(CreatePostRestAdapterRequest adapterRequest, String username) {
            return new CreatePostInputPortRequest(adapterRequest.title, adapterRequest.content, UUID.fromString(adapterRequest.topicId), username);
        }
    }
}
