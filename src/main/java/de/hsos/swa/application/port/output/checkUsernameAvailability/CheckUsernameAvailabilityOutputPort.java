package de.hsos.swa.application.port.output.checkUsernameAvailability;


public interface CheckUsernameAvailabilityOutputPort {
    CheckUsernameAvailabilityOutputPortResponse isUserNameAvailable(CheckUsernameAvailabilityOutputPortRequest outputPortRequest);
}
