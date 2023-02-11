package de.hsos.swa.application.input.dto.in;

import de.hsos.swa.application.annotations.InputPortRequest;
import de.hsos.swa.application.input.validation.constraints.ValidCommentText;
import de.hsos.swa.application.input.validation.constraints.ValidId;

/**
 * Der Record ReplyToCommentCommand definiert das validierte Request-DTO für den Input Port ReplyToCommentUseCase.
 *
 * @author Lorenzo Battiston
 * @author Oliver Schlüter
 * @version 1.0
 * @see de.hsos.swa.application.input.command.ReplyToCommentUseCase         Nutzt dieses Request-DTO
 */
@InputPortRequest
public record ReplyToCommentCommand(
       @ValidId String commentId,
       @ValidCommentText String commentText
) {}
