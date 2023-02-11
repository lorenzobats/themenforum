package de.hsos.swa.application.input.dto.in;

import de.hsos.swa.application.annotations.InputPortRequest;
import javax.validation.constraints.NotNull;

/**
 * Der Record GetAllCommentsQuery definiert das validierte Request-DTO für den Input Port GetAllCommentsUseCase.
 *
 * @author Lorenzo Battiston
 * @author Oliver Schlüter
 * @version 1.0
 * @see de.hsos.swa.application.input.query.GetAllCommentsUseCase         Nutzt dieses Request-DTO
 */
@InputPortRequest
public record GetAllCommentsQuery(
        @NotNull boolean includeReplies
) {}
