package de.hsos.swa.application.output.auth.dto.out;

import de.hsos.swa.application.annotations.OutputPortRequest;

import java.util.UUID;

@OutputPortRequest
public class SaveAuthUserCommand {
    private final String username;
    private final String password;
    private final String role;
    private final UUID userId;


    public SaveAuthUserCommand(String username, String password, String role, UUID userId) {
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
