package de.hsos.swa.application.input.dto.in;

import de.hsos.swa.application.annotations.InputPortRequest;
import de.hsos.swa.application.input.validation.constraints.ValidId;

/**
 * Der Record DeleteVoteCommand definiert das validierte Request-DTO für den Input Port DeleteVoteUseCase.
 *
 * @author Lorenzo Battiston
 * @author Oliver Schlüter
 * @version 1.0
 * @see de.hsos.swa.application.input.command.DeleteVoteUseCase         Nutzt dieses Request-DTO
 */
@InputPortRequest
public record DeleteVoteCommand(
        @ValidId String voteId
) {}
