package de.hsos.swa.application.input;

import de.hsos.swa.application.annotations.InputPort;
import de.hsos.swa.application.input.dto.in.DeleteUserCommand;
import de.hsos.swa.application.input.dto.out.ApplicationResult;
import de.hsos.swa.domain.entity.User;

import javax.validation.Valid;
@InputPort
public interface DeleteUserUseCase {
    ApplicationResult<User> deleteUser(@Valid DeleteUserCommand request);
}
