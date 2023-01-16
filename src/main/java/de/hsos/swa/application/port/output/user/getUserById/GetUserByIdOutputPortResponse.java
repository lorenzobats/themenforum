package de.hsos.swa.application.port.output.user.getUserById;

import java.util.UUID;

public class GetUserByIdOutputPortResponse {
    private final UUID id;
    private final String username;
    public GetUserByIdOutputPortResponse(UUID id, String username) {
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
