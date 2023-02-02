package de.hsos.swa.application.input;

import de.hsos.swa.application.annotations.InputPort;
import de.hsos.swa.application.input.dto.out.ApplicationResult;
import de.hsos.swa.application.input.dto.in.RegisterUserCommand;
import de.hsos.swa.domain.entity.User;

import javax.validation.Valid;
@InputPort
public interface RegisterUserUseCase {
    ApplicationResult<User> registerUser(@Valid RegisterUserCommand request);
}
