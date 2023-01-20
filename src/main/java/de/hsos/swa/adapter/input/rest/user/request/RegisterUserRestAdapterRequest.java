package de.hsos.swa.adapter.input.rest.user.request;

import de.hsos.swa.application.port.input.registerUser.RegisterUserInputPortRequest;

public class RegisterUserRestAdapterRequest {

    public String username;
    public String password;

    public RegisterUserRestAdapterRequest() {
    }

    public RegisterUserRestAdapterRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public static class Converter {
        public static RegisterUserInputPortRequest toInputPortCommand(RegisterUserRestAdapterRequest adapterRequest) {
            return new RegisterUserInputPortRequest(adapterRequest.username, adapterRequest.password);
        }
    }
}
