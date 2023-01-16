package de.hsos.swa.application.port.output.user;


import de.hsos.swa.application.port.input._shared.Result;

public interface CheckUsernameAvailabilityOutputPort {
    Result<Boolean> isUserNameAvailable(String username);
}
