package de.hsos.swa.application.port.input.registerUser;
import de.hsos.swa.application.port.input._shared.SelfValidating;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class RegisterUserInputPortRequest extends SelfValidating<RegisterUserInputPortRequest> {

    @NotEmpty(message = "Username empty")
    @Size(min = 4, max = 20, message = "Username must be between 4 and 20 characters")
    private final String username;

    @NotEmpty(message = "Password empty")
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
