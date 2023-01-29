package de.hsos.swa.infrastructure.rest.dto.in;

import de.hsos.swa.application.input.dto.in.CreatePostInputPortRequest;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.UUID;


public class CreatePostRestAdapterRequest {

    @NotBlank(message = "title is empty")
    public String title;

    @NotBlank(message = "content is empty")
    public String content;

    @NotBlank(message = "topicId is empty")
    public String topicId;

    public CreatePostRestAdapterRequest() {}

    public static class Converter {
        public static CreatePostInputPortRequest toInputPortCommand(CreatePostRestAdapterRequest adapterRequest, String username) {
            return new @Valid CreatePostInputPortRequest(adapterRequest.title, adapterRequest.content, UUID.fromString(adapterRequest.topicId), username);
        }
    }
}
