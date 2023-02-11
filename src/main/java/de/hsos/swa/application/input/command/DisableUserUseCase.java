package de.hsos.swa.application.input.command;

import de.hsos.swa.application.annotations.InputPort;
import de.hsos.swa.application.input.dto.in.DisableUserCommand;
import de.hsos.swa.application.input.dto.out.ApplicationResult;
import de.hsos.swa.domain.entity.User;

import javax.validation.Valid;
import java.util.Optional;

/**
 * Das Interface DisableUserUseCase definiert den Input Port für InteraktionsAdapter zur Nutzung des
 * DisableUserService im Application-Hexagon.
 *
 * @author Lorenzo Battiston
 * @author Oliver Schlüter
 * @version 1.0
 * @see de.hsos.swa.application.service.command.DisableUserService    Implementierender Service dieses Input Ports
 * @see DisableUserCommand                                            Übergebenes Request-DTO an diesen Input Port
 */
@InputPort
public interface DisableUserUseCase {
    ApplicationResult<Optional<User>> deleteUser(@Valid DisableUserCommand command, String requestingUser);
}
