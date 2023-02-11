package de.hsos.swa.application.input.dto.in;

import de.hsos.swa.application.annotations.InputPortRequest;
import de.hsos.swa.application.input.validation.constraints.ValidUsername;

/**
 * Der Record GetCommentsByUserQuery definiert das validierte Request-DTO für den Input Port GetCommentsByUserUseCase.
 *
 * @author Lorenzo Battiston
 * @author Oliver Schlüter
 * @version 1.0
 * @see de.hsos.swa.application.input.query.GetCommentsByUserUseCase         Nutzt dieses Request-DTO
 */
@InputPortRequest
public record GetCommentsByUserQuery(
        @ValidUsername String username
) {}
