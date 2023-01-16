package de.hsos.swa.application.port.input.registerUser;

import de.hsos.swa.application.port.input._shared.Result;

import java.util.UUID;

public class RegisterUserInputPortResponse extends Result<RegisterUserInputPortResponse> {
    private final UUID id;
    private final String username;

    public RegisterUserInputPortResponse(UUID id, String username) {
        this.id = id;
        this.username = username;
    }
    public UUID getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }
}
