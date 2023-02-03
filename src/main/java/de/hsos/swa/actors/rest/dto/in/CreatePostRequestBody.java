package de.hsos.swa.actors.rest.dto.in;

import de.hsos.swa.actors.rest.dto.in.validation.ValidatedRequestBody;
import de.hsos.swa.application.input.dto.in.CreatePostCommand;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.UUID;

@Schema(name = "PostCreationDto")
public class CreatePostRequestBody implements ValidatedRequestBody {

    @NotBlank(message = "title is blank")
    public String title;

    @NotBlank(message = "content is blank")
    public String content;

    @NotBlank(message = "topicId is blank")
    public String topicId;

    public CreatePostRequestBody() {}

    public static class Converter {
        public static CreatePostCommand toInputPortCommand(CreatePostRequestBody adapterRequest, String username) {
            return new @Valid CreatePostCommand(adapterRequest.title, adapterRequest.content, adapterRequest.topicId, username);
        }
    }
}
