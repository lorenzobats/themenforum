package de.hsos.swa.infrastructure.rest.dto.in;

import de.hsos.swa.application.input.dto.in.CreateTopicInputPortRequest;
import javax.validation.constraints.NotBlank;


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