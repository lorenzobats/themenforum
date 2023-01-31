package de.hsos.swa.actors.ui.dto.in;

import de.hsos.swa.application.input.dto.in.CreatePostCommand;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import javax.validation.constraints.NotBlank;
import java.util.UUID;

@Schema(hidden = true)
public class CreatePostUIRequest implements UIRequest{

    @NotBlank(message = "title is empty")
    public String title;
    @NotBlank(message = "content is empty")
    public String content;
    @NotBlank(message = "topicId is empty")
    public String topic;

    public CreatePostUIRequest() {}

    public static class Converter {
        public static CreatePostCommand toInputPortCommand(CreatePostUIRequest adapterRequest, String username) {
            return new CreatePostCommand(adapterRequest.title, adapterRequest.content, UUID.fromString(adapterRequest.topic), username);
        }
    }
}
