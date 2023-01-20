package de.hsos.swa.adapter.input.rest.user.response;

import de.hsos.swa.domain.entity.User;

public class GetUserByNameRestAdapterResponse {
    public String id;
    public String username;


    public GetUserByNameRestAdapterResponse() {
    }

    public GetUserByNameRestAdapterResponse(String id, String username) {
        this.id = id;
        this.username = username;

    }

    public static class Converter {
        public static GetUserByNameRestAdapterResponse fromUseCaseResult(User user) {
            return new GetUserByNameRestAdapterResponse(user.getId().toString(), user.getName());
        }
    }
}
