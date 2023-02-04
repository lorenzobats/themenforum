package de.hsos.swa.actors.rest.dto.in;

import de.hsos.swa.actors.rest.dto.in.validation.ValidatedRequestBody;
import de.hsos.swa.application.input.dto.in.CreateTopicCommand;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import javax.validation.constraints.NotBlank;

@Schema(name = "TopicCreationDto")
public class CreateTopicRequestBody implements ValidatedRequestBody {

    @NotBlank(message = "title is blank")
    public String title;

    @NotBlank(message = "description is blank")
    public String description;

    public CreateTopicRequestBody() {}

    public static class Converter {
        public static CreateTopicCommand toInputPortCommand(CreateTopicRequestBody adapterRequest) {
            return new CreateTopicCommand(adapterRequest.title, adapterRequest.description);
        }
    }
}
