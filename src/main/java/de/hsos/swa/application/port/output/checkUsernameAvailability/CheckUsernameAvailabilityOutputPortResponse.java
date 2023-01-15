package de.hsos.swa.application.port.output.checkUsernameAvailability;

public class CheckUsernameAvailabilityOutputPortResponse {
    private final boolean userNameAvailable;

    public CheckUsernameAvailabilityOutputPortResponse(boolean userNameAvailable) {
        this.userNameAvailable = userNameAvailable;
    }

    public boolean isUserNameAvailable() {
        return userNameAvailable;
    }
}
