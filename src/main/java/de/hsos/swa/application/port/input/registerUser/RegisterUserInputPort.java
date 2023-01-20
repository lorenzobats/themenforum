package de.hsos.swa.application.port.input.registerUser;

import de.hsos.swa.application.port.input._shared.Result;

import javax.validation.Valid;

public interface RegisterUserInputPort {
    Result<RegisterUserInputPortResponse> registerUser(@Valid RegisterUserInputPortRequest inputPortRequest);
}
