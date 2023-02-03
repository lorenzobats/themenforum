package de.hsos.swa.application.input.dto.in;

import de.hsos.swa.application.annotations.InputPortRequest;
import de.hsos.swa.application.input.validation.constraints.ValidId;
import de.hsos.swa.application.input.validation.constraints.ValidPostContent;
import de.hsos.swa.application.input.validation.constraints.ValidPostTitle;
import de.hsos.swa.application.input.validation.constraints.ValidUsername;

import java.util.UUID;

@InputPortRequest
public record CreatePostCommand(
        @ValidPostTitle String title,
        @ValidPostContent String content,
        @ValidId String topicId,
        @ValidUsername String username
) {}
