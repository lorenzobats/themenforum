package de.hsos.swa.infrastructure.rest.dto.in;

import de.hsos.swa.application.input.dto.in.CreateTopicInputPortRequest;
import javax.validation.constraints.NotBlank;


public class CreateTopicRestAdapterRequest {

    @NotBlank(message = "Topic Titel darf nicht leer sein")
    public String title;

    @NotBlank(message = "Topic Description darf nicht leer sein")
    public String description;

    public CreateTopicRestAdapterRequest() {}

    public static class Converter {
        public static CreateTopicInputPortRequest toInputPortCommand(CreateTopicRestAdapterRequest adapterRequest, String username) {
            return new CreateTopicInputPortRequest(adapterRequest.title, adapterRequest.description, username);
        }
    }
}
