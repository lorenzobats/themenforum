package de.hsos.swa.application.input.dto.in;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;


public class DeletePostInputPortRequest {

    @Pattern(regexp = "[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}", message = "Post ID is not valid.")
    private final String id;

    @NotBlank(message = "username is empty")
    private final String username;

    public DeletePostInputPortRequest(String id, String username) {
        this.id = id;
        this.username = username;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }
}
