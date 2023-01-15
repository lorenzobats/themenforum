package de.hsos.swa.application.port.input.getUserByName;
import java.util.UUID;
public class GetUserByNameInputPortResponse {
    private final UUID id;
    private final String username;
    public GetUserByNameInputPortResponse(UUID id, String username) {
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
