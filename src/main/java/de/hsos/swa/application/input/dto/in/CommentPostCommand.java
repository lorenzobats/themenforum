package de.hsos.swa.application.input.dto.in;

import de.hsos.swa.application.annotations.InputPortRequest;
import de.hsos.swa.domain.validation.constraints.ValidId;
import de.hsos.swa.domain.validation.constraints.ValidUsername;

import javax.validation.constraints.Size;

@InputPortRequest
public record CommentPostCommand(
        @ValidId(message = "Invalid 'postId'")
        String postId,
        @ValidUsername(message = "Invalid 'username'")
        String username,
        @Size(max = 250, message = "Text is limited to 250 Characters")
        String commentText
){}