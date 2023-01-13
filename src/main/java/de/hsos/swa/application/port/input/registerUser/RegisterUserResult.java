package de.hsos.swa.application.port.input.registerUser;

import java.util.UUID;

public class RegisterUserResult {

    private final UUID id;
    private final String username;

    public RegisterUserResult(UUID id, String username) {
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
