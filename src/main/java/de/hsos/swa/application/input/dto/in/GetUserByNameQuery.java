package de.hsos.swa.application.input.dto.in;

import de.hsos.swa.application.annotations.InputPortRequest;
import de.hsos.swa.application.input.validation.constraints.ValidUsername;

/**
 * Der Record GetUserByNameQuery definiert das validierte Request-DTO für den Input Port GetUserByNameUseCase.
 *
 * @author Lorenzo Battiston
 * @author Oliver Schlüter
 * @version 1.0
 * @see de.hsos.swa.application.input.query.GetUserByNameUseCase         Nutzt dieses Request-DTO
 */
@InputPortRequest
public record GetUserByNameQuery(
        @ValidUsername String username
) {}
