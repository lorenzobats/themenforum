package de.hsos.swa.application.input.dto.in;

import de.hsos.swa.application.annotations.InputPortRequest;
import de.hsos.swa.application.input.validation.constraints.ValidCommentText;
import de.hsos.swa.application.input.validation.constraints.ValidId;

@InputPortRequest
public record CommentPostCommand(
        @ValidId String postId,
        @ValidCommentText String commentText
){}