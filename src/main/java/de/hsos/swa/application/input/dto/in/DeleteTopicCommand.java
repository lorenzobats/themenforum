package de.hsos.swa.application.input.dto.in;

import de.hsos.swa.application.annotations.InputPortRequest;
import de.hsos.swa.application.input.validation.constraints.ValidId;
import de.hsos.swa.application.input.validation.constraints.ValidUsername;

/**
 * Der Record DeleteTopicCommand definiert das validierte Request-DTO für den Input Port DeleteTopicUseCase.
 *
 * @author Lorenzo Battiston
 * @author Oliver Schlüter
 * @version 1.0
 * @see de.hsos.swa.application.input.command.DeleteTopicUseCase         Nutzt dieses Request-DTO
 */
@InputPortRequest
public record DeleteTopicCommand(
        @ValidId String id,
        @ValidUsername String username
) {}
