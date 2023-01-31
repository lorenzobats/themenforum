package de.hsos.swa.actors.rest.dto.in;

import de.hsos.swa.application.input.dto.in.CreateTopicInputPortRequest;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import javax.validation.constraints.NotBlank;

@Schema(name = "TopicCreationDTO")
public class CreateTopicRestAdapterRequest {

    @NotBlank(message = "title is empty")
    public String title;

    @NotBlank(message = "description is empty")
    public String description;

    public CreateTopicRestAdapterRequest() {}

    public static class Converter {
        public static CreateTopicInputPortRequest toInputPortCommand(CreateTopicRestAdapterRequest adapterRequest, String username) {
            return new CreateTopicInputPortRequest(adapterRequest.title, adapterRequest.description, username);
        }
    }
}
