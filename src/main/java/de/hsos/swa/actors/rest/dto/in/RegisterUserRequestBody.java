package de.hsos.swa.actors.rest.dto.in;

import de.hsos.swa.actors.rest.dto.in.validation.ValidatedRequestBody;
import de.hsos.swa.application.input.dto.in.RegisterUserCommand;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "UserCreationDto")
public class RegisterUserRequestBody implements ValidatedRequestBody {

    public String username;

    public String password;

    public static class Converter {
        public static RegisterUserCommand toInputPortCommand(RegisterUserRequestBody adapterRequest) {
            return new RegisterUserCommand(adapterRequest.username, adapterRequest.password);
        }
    }
}
