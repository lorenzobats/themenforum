package de.hsos.swa.application.input.dto.in;

import de.hsos.swa.application.annotations.InputPortRequest;
import de.hsos.swa.application.input.validation.constraints.ValidId;

/**
 * Der Record DisableUserCommand definiert das validierte Request-DTO für den Input Port DisableUserUseCase.
 *
 * @author Lorenzo Battiston
 * @author Oliver Schlüter
 * @version 1.0
 * @see de.hsos.swa.application.input.command.DisableUserUseCase         Nutzt dieses Request-DTO
 */
@InputPortRequest
public record DisableUserCommand(
        @ValidId String userId
) {}
