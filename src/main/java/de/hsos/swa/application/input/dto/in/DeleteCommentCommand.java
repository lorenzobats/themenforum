package de.hsos.swa.application.input.dto.in;

import de.hsos.swa.application.annotations.InputPortRequest;
import de.hsos.swa.application.input.validation.constraints.ValidId;

/**
 * Der Record DeleteCommentCommand definiert das validierte Request-DTO für den Input Port DeleteCommentUseCase.
 *
 * @author Lorenzo Battiston
 * @author Oliver Schlüter
 * @version 1.0
 * @see de.hsos.swa.application.input.command.DeleteCommentUseCase         Nutzt dieses Request-DTO
 */
@InputPortRequest
public record DeleteCommentCommand(
        @ValidId String commentId
) { }
