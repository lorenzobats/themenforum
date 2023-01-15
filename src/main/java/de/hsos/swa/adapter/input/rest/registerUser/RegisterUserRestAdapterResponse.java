package de.hsos.swa.adapter.input.rest.registerUser;

import de.hsos.swa.application.port.input.registerUser.RegisterUserInputPortResponse;

public class RegisterUserRestAdapterResponse {

    public String id;
    public String username;


    public RegisterUserRestAdapterResponse() {
    }

    public RegisterUserRestAdapterResponse(String id, String username) {
        this.id = id;
        this.username = username;

    }

    public static class Converter {
        public static RegisterUserRestAdapterResponse fromUseCaseResult(RegisterUserInputPortResponse registerUserInputPortResponse) {
            return new RegisterUserRestAdapterResponse(registerUserInputPortResponse.getId().toString(), registerUserInputPortResponse.getUsername());
        }
    }
}
