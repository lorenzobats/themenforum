package de.hsos.swa.application.port.output.getUserByName;

import java.util.UUID;

public class GetUserByNameOutputPortResponse {
    private final UUID id;
    private final String username;
    public GetUserByNameOutputPortResponse(UUID id, String username) {
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
