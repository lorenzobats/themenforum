package de.hsos.swa.application.port.input.getUserByName;

import de.hsos.swa.application.port.input._shared.SelfValidating;

import javax.validation.constraints.NotEmpty;

public class GetUserByNameInputPortRequest extends SelfValidating<GetUserByNameInputPortRequest> {
    @NotEmpty(message = "username empty")
    private final String username;

    public GetUserByNameInputPortRequest(String username) {
        this.username = username;
        this.validateSelf();
    }

    public String getUsername() {
        return username;
    }
}
