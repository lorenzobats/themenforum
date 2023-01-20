package de.hsos.swa.application.port.input.createPost;

import javax.validation.constraints.NotEmpty;

public class CreatePostInputPortRequest {

    @NotEmpty(message="Field: 'title' is missing")
    private final String title;

    @NotEmpty(message = "Field: 'username' is missing")
    private final String username;

    public CreatePostInputPortRequest(String title, String username) {
        this.title = title;
        this.username = username;
    }

    public String getTitle() {
        return title;
    }

    public String getUsername() {
        return username;
    }
}
