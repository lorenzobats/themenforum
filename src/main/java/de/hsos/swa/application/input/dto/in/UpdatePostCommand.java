package de.hsos.swa.application.input.dto.in;

import de.hsos.swa.application.annotations.InputPortRequest;
import de.hsos.swa.application.input.validation.constraints.ValidId;
import de.hsos.swa.application.input.validation.constraints.ValidPostContent;
import de.hsos.swa.application.input.validation.constraints.ValidPostTitle;

/**
 * Der Record UpdatePostCommand definiert das validierte Request-DTO für den Input Port UpdatePostUseCase.
 *
 * @author Lorenzo Battiston
 * @author Oliver Schlüter
 * @version 1.0
 * @see de.hsos.swa.application.input.command.UpdatePostUseCase         Nutzt dieses Request-DTO
 */
@InputPortRequest
public record UpdatePostCommand(
        @ValidId String postId,
        @ValidPostTitle(nullable = true) String title,
        @ValidPostContent(nullable = true) String content
) {}
