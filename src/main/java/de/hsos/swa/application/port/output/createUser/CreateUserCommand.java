package de.hsos.swa.application.port.output.createUser;

public class CreateUserCommand {
    private final String username;

    public CreateUserCommand(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
