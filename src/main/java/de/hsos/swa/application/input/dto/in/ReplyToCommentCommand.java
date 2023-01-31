package de.hsos.swa.application.input.dto.in;

import de.hsos.swa.application.annotations.InputPortRequest;
import de.hsos.swa.domain.validation.constraints.ValidId;
import de.hsos.swa.domain.validation.constraints.ValidUsername;
@InputPortRequest
public record ReplyToCommentCommand(
       @ValidId
       String commentId,
        @ValidUsername
        String username,

        // TODO: CommentText Validierung wie CommentPostService
        String commentText
) {}
