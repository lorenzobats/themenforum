package de.hsos.swa.application.port.output.createUser;

import java.util.UUID;

public class CreateUserResult {

    private final UUID id;

    private final String username;

    public CreateUserResult(UUID id, String username) {
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
