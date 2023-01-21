package de.hsos.swa.infrastructure.rest.request;

import de.hsos.swa.application.input.request.RegisterUserInputPortRequest;

import javax.validation.constraints.NotBlank;

public class RegisterUserRestAdapterRequest {

    @NotBlank
    public String username;

    @NotBlank
    public String password;

    public RegisterUserRestAdapterRequest() {
    }

    public static class Converter {
        public static RegisterUserInputPortRequest toInputPort(RegisterUserRestAdapterRequest adapterRequest) {
            return new RegisterUserInputPortRequest(adapterRequest.username, adapterRequest.password);
        }
    }
}
