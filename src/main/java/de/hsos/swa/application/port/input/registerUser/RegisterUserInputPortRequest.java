package de.hsos.swa.application.port.input.registerUser;
import de.hsos.swa.application.port.input._shared.SelfValidating;
import javax.validation.constraints.NotEmpty;

public class RegisterUserInputPortRequest extends SelfValidating<RegisterUserInputPortRequest> {
    @NotEmpty(message = "username empty")
    private final String username;
    @NotEmpty(message = "password empty")
    private final String password;

    public RegisterUserInputPortRequest(String username, String password) {
        this.username = username;
        this.password = password;
        this.validateSelf();
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
