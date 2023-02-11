package de.hsos.swa.application.input.dto.in;

import de.hsos.swa.application.annotations.InputPortRequest;
import de.hsos.swa.application.input.validation.constraints.ValidId;
import de.hsos.swa.application.input.validation.constraints.ValidPostContent;
import de.hsos.swa.application.input.validation.constraints.ValidPostTitle;

/**
 * Der Record CreatePostCommand definiert das validierte Request-DTO für den Input Port CreatePostUseCase.
 *
 * @author Lorenzo Battiston
 * @author Oliver Schlüter
 * @version 1.0
 * @see de.hsos.swa.application.input.command.CreatePostUseCase         Nutzt dieses Request-DTO
 */
@InputPortRequest
public record CreatePostCommand(
        @ValidPostTitle String title,
        @ValidPostContent String content,
        @ValidId String topicId
) {}
