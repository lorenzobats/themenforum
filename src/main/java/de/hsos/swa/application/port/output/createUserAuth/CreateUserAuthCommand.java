package de.hsos.swa.application.port.output.createUserAuth;

import java.util.UUID;

public class CreateUserAuthCommand {
    private final String username;
    private final String password;

    private final String role;

    private final UUID userId;


    public CreateUserAuthCommand(String username, String password, String role, UUID userId) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public UUID getUserId() {
        return userId;
    }
}
