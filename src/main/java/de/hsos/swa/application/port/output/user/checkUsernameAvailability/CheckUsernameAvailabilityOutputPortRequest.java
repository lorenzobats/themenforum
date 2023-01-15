package de.hsos.swa.application.port.output.user.checkUsernameAvailability;

import de.hsos.swa.application.port.input._shared.SelfValidating;

import javax.validation.constraints.NotEmpty;

public class CheckUsernameAvailabilityOutputPortRequest extends SelfValidating<CheckUsernameAvailabilityOutputPortRequest> {
    @NotEmpty(message = "username empty")
    private final String username;

    public CheckUsernameAvailabilityOutputPortRequest(String username) {
        this.username = username;
        this.validateSelf();
    }

    public String getUsername() {
        return username;
    }
}
