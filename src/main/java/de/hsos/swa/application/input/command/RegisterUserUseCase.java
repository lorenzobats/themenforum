package de.hsos.swa.application.input.command;

import de.hsos.swa.application.annotations.InputPort;
import de.hsos.swa.application.input.dto.out.ApplicationResult;
import de.hsos.swa.application.input.dto.in.RegisterUserCommand;
import de.hsos.swa.domain.entity.User;

import javax.validation.Valid;

/**
 * Das Interface RegisterUserUseCase definiert den Input Port für InteraktionsAdapter zur Nutzung des
 * RegisterUserService im Application-Hexagon.
 *
 * @author Lorenzo Battiston
 * @author Oliver Schlüter
 * @version 1.0
 * @see de.hsos.swa.application.service.command.RegisterUserService    Implementierender Service dieses Input Ports
 * @see RegisterUserCommand                                            Übergebenes Request-DTO an diesen Input Port
 */
@InputPort
public interface RegisterUserUseCase {
    ApplicationResult<User> registerUser(@Valid RegisterUserCommand command);
}
