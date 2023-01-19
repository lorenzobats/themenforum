package de.hsos.swa.adapter.input.rest.response;

import de.hsos.swa.application.port.input.getUserByName.GetUserByNameInputPortResponse;

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
        public static GetUserByNameRestAdapterResponse fromUseCaseResult(GetUserByNameInputPortResponse registerUserResult) {
            return new GetUserByNameRestAdapterResponse(registerUserResult.getId().toString(), registerUserResult.getUsername());
        }
    }
}
