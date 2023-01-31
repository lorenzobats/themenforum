package de.hsos.swa.application.input;

import de.hsos.swa.application.annotations.InputPort;
import de.hsos.swa.application.input.dto.out.Result;
import de.hsos.swa.application.input.dto.in.RegisterUserCommand;
import de.hsos.swa.domain.entity.User;

import javax.validation.Valid;
@InputPort
public interface RegisterUserUseCase {
    Result<User> registerUser(@Valid RegisterUserCommand request);
}
