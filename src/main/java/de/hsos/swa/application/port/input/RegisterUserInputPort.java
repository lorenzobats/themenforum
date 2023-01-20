package de.hsos.swa.application.port.input;

import de.hsos.swa.application.port.input.request.RegisterUserInputPortRequest;
import de.hsos.swa.domain.entity.User;

import javax.validation.Valid;

public interface RegisterUserInputPort {
    Result<User> registerUser(@Valid RegisterUserInputPortRequest request);
}
