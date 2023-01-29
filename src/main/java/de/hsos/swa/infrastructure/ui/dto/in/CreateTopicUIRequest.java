package de.hsos.swa.infrastructure.ui.dto.in;

import de.hsos.swa.application.input.dto.in.CreateTopicInputPortRequest;

import javax.validation.constraints.NotBlank;


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
