package de.hsos.swa.actors.ui.dto.in;

import de.hsos.swa.application.input.dto.in.CreateTopicInputPortRequest;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import javax.validation.constraints.NotBlank;

@Schema(hidden = true)
public class CreateTopicUIRequest implements UIRequest{

    @NotBlank(message = "title is empty")
    public String title;
    @NotBlank(message = "description is empty")
    public String description;

    public CreateTopicUIRequest() {}

    public static class Converter {
        public static CreateTopicInputPortRequest toInputPortCommand(CreateTopicUIRequest adapterRequest, String username) {
            return new CreateTopicInputPortRequest(adapterRequest.title, adapterRequest.description, username);
        }
    }
}
