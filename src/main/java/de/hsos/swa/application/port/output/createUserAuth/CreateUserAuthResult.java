package de.hsos.swa.application.port.output.createUserAuth;

import java.util.UUID;

public class CreateUserAuthResult {

    private final UUID id;

    private final String username;

    public CreateUserAuthResult(UUID id, String username) {
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
