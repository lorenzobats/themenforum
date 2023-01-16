package de.hsos.swa.application.port.output.user.getUserByName;

import de.hsos.swa.application.port.input._shared.SelfValidating;

import javax.validation.constraints.NotEmpty;

public class GetUserByNameOutputPortRequest extends SelfValidating<GetUserByNameOutputPortRequest> {

    private final String username;

    public GetUserByNameOutputPortRequest(String username) {
        this.username = username;
        this.validateSelf();
    }

    public String getUsername() {
        return username;
    }
}
