package de.hsos.swa.application.output.auth.createUserAuth;

import java.util.UUID;

public class CreateUserAuthOutputPortResponse {

    private final UUID id;

    private final String username;

    public CreateUserAuthOutputPortResponse(UUID id, String username) {
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
