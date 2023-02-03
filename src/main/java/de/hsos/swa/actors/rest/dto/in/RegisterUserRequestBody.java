package de.hsos.swa.actors.rest.dto.in;

import de.hsos.swa.actors.rest.dto.in.validation.ValidatedRequestBody;
import de.hsos.swa.application.input.dto.in.RegisterUserCommand;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import javax.validation.constraints.NotBlank;

@Schema(name = "UserRegistrationDto")
public class RegisterUserRequestBody implements ValidatedRequestBody {

    @NotBlank(message = "username is blank")
    public String username;

    @NotBlank(message = "password is blank")
    public String password;

    public static class Converter {
        public static RegisterUserCommand toInputPortCommand(RegisterUserRequestBody adapterRequest) {
            return new RegisterUserCommand(adapterRequest.username, adapterRequest.password);
        }
    }
}
