package de.hsos.swa.adapter.input.rest.user.response;

import de.hsos.swa.domain.entity.User;

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
        public static RegisterUserRestAdapterResponse fromUseCaseResult(User user) {
            return new RegisterUserRestAdapterResponse(user.getId().toString(), user.getName());
        }
    }
}
