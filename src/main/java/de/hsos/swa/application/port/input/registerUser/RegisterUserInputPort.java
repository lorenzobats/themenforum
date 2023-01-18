package de.hsos.swa.application.port.input.registerUser;

import de.hsos.swa.application.port.input._shared.Result;

public interface RegisterUserInputPort {
    Result<RegisterUserInputPortResponse> registerUser(RegisterUserInputPortRequest inputPortRequest);
}
