package de.hsos.swa.application.input.command;

import de.hsos.swa.application.annotations.InputPort;
import de.hsos.swa.application.input.dto.in.DisableUserCommand;
import de.hsos.swa.application.input.dto.out.ApplicationResult;
import de.hsos.swa.domain.entity.User;

import javax.validation.Valid;
import java.util.Optional;

@InputPort
public interface DisableUserUseCase {
    ApplicationResult<Optional<User>> deleteUser(@Valid DisableUserCommand command, String requestingUser);
}
