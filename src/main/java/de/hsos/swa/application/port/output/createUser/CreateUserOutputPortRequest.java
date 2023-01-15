package de.hsos.swa.application.port.output.createUser;

public class CreateUserOutputPortRequest {
    private final String username;

    public CreateUserOutputPortRequest(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
