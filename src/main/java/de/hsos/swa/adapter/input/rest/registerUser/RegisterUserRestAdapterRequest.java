package de.hsos.swa.adapter.input.rest.registerUser;

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
        public static RegisterUserInputPortRequest toUseCaseCommand(RegisterUserRestAdapterRequest request) {
            return new RegisterUserInputPortRequest(request.username, request.password);
        }
    }
}
