package de.hsos.swa.application.port.input.getUserByName;

import javax.validation.constraints.NotEmpty;

public class GetUserByNameInputPortRequest {
    @NotEmpty(message = "username empty")
    private final String username;

    public GetUserByNameInputPortRequest(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
