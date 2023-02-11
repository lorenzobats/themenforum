package de.hsos.swa.application.input.dto.in;

import de.hsos.swa.application.annotations.InputPortRequest;
import de.hsos.swa.application.input.validation.constraints.ValidUsername;

/**
 * Der Record GetAllVotesByUsernameQuery definiert das validierte Request-DTO für den Input Port GetAllVotesByUsernameUseCase.
 *
 * @author Lorenzo Battiston
 * @author Oliver Schlüter
 * @version 1.0
 * @see de.hsos.swa.application.input.query.GetAllVotesByUsernameUseCase         Nutzt dieses Request-DTO
 */
@InputPortRequest
public record GetAllVotesByUsernameQuery(
        @ValidUsername String username
) {}
