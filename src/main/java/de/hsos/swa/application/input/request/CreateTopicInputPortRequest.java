package de.hsos.swa.application.input.request;

import javax.validation.constraints.NotEmpty;

public class CreateTopicInputPortRequest {

    @NotEmpty(message="Field: 'title' is missing")
    private final String title;

    @NotEmpty(message="Field: 'description' is missing")
    private final String description;

    @NotEmpty(message = "Field: 'username' is missing")
    private final String username;

    public CreateTopicInputPortRequest(String title, String description, String username) {
        this.title = title;
        this.description = description;
        this.username = username;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getUsername() {
        return username;
    }
}
