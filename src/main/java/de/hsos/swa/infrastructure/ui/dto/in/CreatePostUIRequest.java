package de.hsos.swa.infrastructure.ui.dto.in;

import de.hsos.swa.application.input.dto.in.CreatePostInputPortRequest;

import javax.validation.constraints.NotBlank;
import java.util.UUID;


public class CreatePostUIRequest implements UIRequest{

    @NotBlank(message = "title is empty")
    public String title;
    @NotBlank(message = "content is empty")
    public String content;
    @NotBlank(message = "topicId is empty")
    public String topic;

    public CreatePostUIRequest() {}

    public static class Converter {
        public static CreatePostInputPortRequest toInputPortCommand(CreatePostUIRequest adapterRequest, String username) {
            return new CreatePostInputPortRequest(adapterRequest.title, adapterRequest.content, UUID.fromString(adapterRequest.topic), username);
        }
    }
}
