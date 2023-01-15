package de.hsos.swa.application.port.output.user.checkUsernameAvailability;


import de.hsos.swa.application.port.input._shared.Result;

public interface CheckUsernameAvailabilityOutputPort {
    Result<CheckUsernameAvailabilityOutputPortResponse> isUserNameAvailable(CheckUsernameAvailabilityOutputPortRequest outputPortRequest);
}
