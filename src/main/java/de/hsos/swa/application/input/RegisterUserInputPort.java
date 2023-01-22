package de.hsos.swa.application.input;

import de.hsos.swa.application.output.Result;
import de.hsos.swa.application.input.dto.in.RegisterUserInputPortRequest;
import de.hsos.swa.domain.entity.User;

import javax.validation.Valid;

public interface RegisterUserInputPort {
    Result<User> registerUser(@Valid RegisterUserInputPortRequest request);
}
