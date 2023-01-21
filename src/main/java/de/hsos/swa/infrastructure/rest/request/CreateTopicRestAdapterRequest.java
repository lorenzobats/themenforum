package de.hsos.swa.infrastructure.rest.request;

import de.hsos.swa.application.input.request.CreateTopicInputPortRequest;
import javax.validation.constraints.NotBlank;


public class CreateTopicRestAdapterRequest {

    @NotBlank(message = "Topic Titel darf nicht leer sein")
    public String title;

    @NotBlank(message = "Topic Description darf nicht leer sein")
    public String description;

    public CreateTopicRestAdapterRequest() {}

    public static class Converter {
        public static CreateTopicInputPortRequest toInputPort(CreateTopicRestAdapterRequest adapterRequest, String username) {
            return new CreateTopicInputPortRequest(adapterRequest.title, adapterRequest.description, username);
        }
    }
}
