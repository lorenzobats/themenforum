package de.hsos.swa.infrastructure.rest.dto.in;

import de.hsos.swa.application.input.dto.in.RegisterUserInputPortRequest;

import javax.validation.constraints.NotBlank;

public class RegisterUserRestAdapterRequest {

    @NotBlank(message = "username is empty")
    public String username;

    @NotBlank(message = "password is empty")
    public String password;

    public RegisterUserRestAdapterRequest() {
    }

    public static class Converter {
        public static RegisterUserInputPortRequest toInputPortCommand(RegisterUserRestAdapterRequest adapterRequest) {
            return new RegisterUserInputPortRequest(adapterRequest.username, adapterRequest.password);
        }
    }
}
