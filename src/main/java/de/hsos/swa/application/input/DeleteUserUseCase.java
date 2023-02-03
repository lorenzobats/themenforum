package de.hsos.swa.application.input;

import de.hsos.swa.application.annotations.InputPort;
import de.hsos.swa.application.input.dto.in.DeleteUserCommand;
import de.hsos.swa.application.input.dto.out.ApplicationResult;
import de.hsos.swa.domain.entity.User;

import javax.validation.Valid;
import java.util.Optional;

@InputPort
public interface DeleteUserUseCase {
    ApplicationResult<Optional<User>> deleteUser(@Valid DeleteUserCommand command, String requestingUser);
}
