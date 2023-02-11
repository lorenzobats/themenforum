package de.hsos.swa.application.input.dto.in;

import de.hsos.swa.application.annotations.InputPortRequest;
import de.hsos.swa.application.input.validation.constraints.ValidCommentText;
import de.hsos.swa.application.input.validation.constraints.ValidId;

/**
 * Der Record CommentPostCommand definiert das validierte Request-DTO für den Input Port CommentPostUseCase.
 *
 * @author Lorenzo Battiston
 * @author Oliver Schlüter
 * @version 1.0
 * @see de.hsos.swa.application.input.command.CommentPostUseCase         Nutzt dieses Request-DTO
 */
@InputPortRequest
public record CommentPostCommand(
        @ValidId String postId,
        @ValidCommentText String commentText
){}