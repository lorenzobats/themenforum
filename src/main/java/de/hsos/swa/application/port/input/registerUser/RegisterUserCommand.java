package de.hsos.swa.application.port.input.registerUser;

import static java.util.Objects.requireNonNull;

public class RegisterUserCommand {
    private final String username;
    private final String password;

    public RegisterUserCommand(String username, String password) {
        this.username = username;
        this.password = password;
        requireNonNull(username);
        requireNonNull(password);
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
