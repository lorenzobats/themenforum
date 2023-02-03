package de.hsos.swa.application.input.dto.in;

import de.hsos.swa.application.annotations.InputPortRequest;
import de.hsos.swa.application.input.validation.constraints.ValidId;
import de.hsos.swa.application.input.validation.constraints.ValidPostContent;
import de.hsos.swa.application.input.validation.constraints.ValidPostTitle;

@InputPortRequest
public record UpdatePostCommand(
        @ValidId String postId,
        @ValidPostTitle String title,
        @ValidPostContent String content
) {}
