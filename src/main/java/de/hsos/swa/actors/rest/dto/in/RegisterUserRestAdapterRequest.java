package de.hsos.swa.actors.rest.dto.in;

import de.hsos.swa.application.input.dto.in.RegisterUserInputPortRequest;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "UserCreationDTO")
public class RegisterUserRestAdapterRequest {

    public String username;

    public String password;

    public static class Converter {
        public static RegisterUserInputPortRequest toInputPortCommand(RegisterUserRestAdapterRequest adapterRequest) {
            return new RegisterUserInputPortRequest(adapterRequest.username, adapterRequest.password);
        }
    }
}
