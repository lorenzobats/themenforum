package de.hsos.swa.application.port.output.user.getUserById;

import de.hsos.swa.application.port.input._shared.SelfValidating;

import javax.validation.constraints.NotEmpty;

public class GetUserByIdOutputPortRequest extends SelfValidating<GetUserByIdOutputPortRequest> {

    private final String userId;

    public GetUserByIdOutputPortRequest(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }
}
