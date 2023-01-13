package de.hsos.swa.adapter.input.rest.registerUser;

import de.hsos.swa.application.port.input.registerUser.RegisterUserResult;

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
        public static RegisterUserRestAdapterResponse fromUseCaseResult(RegisterUserResult registerUserResult) {
            return new RegisterUserRestAdapterResponse(registerUserResult.getId().toString(), registerUserResult.getUsername());
        }
    }
}
