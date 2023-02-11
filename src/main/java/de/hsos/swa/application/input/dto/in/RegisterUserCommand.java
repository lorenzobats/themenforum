package de.hsos.swa.application.input.dto.in;

import de.hsos.swa.application.annotations.InputPortRequest;
import de.hsos.swa.application.input.validation.constraints.ValidPassword;
import de.hsos.swa.application.input.validation.constraints.ValidUsername;

/**
 * Der Record RegisterUserCommand definiert das validierte Request-DTO für den Input Port RegisterUserUseCase.
 *
 * @author Lorenzo Battiston
 * @author Oliver Schlüter
 * @version 1.0
 * @see de.hsos.swa.application.input.command.RegisterUserUseCase         Nutzt dieses Request-DTO
 */
@InputPortRequest
public record RegisterUserCommand(
        @ValidUsername String username,
        @ValidPassword String password
) {}
