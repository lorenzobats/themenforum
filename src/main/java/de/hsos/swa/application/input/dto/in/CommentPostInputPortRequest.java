package de.hsos.swa.application.input.dto.in;

import de.hsos.swa.application.input.validation.constraints.ValidId;
import de.hsos.swa.application.input.validation.constraints.ValidInputPortRequest;
import de.hsos.swa.application.input.validation.constraints.ValidUsername;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;


@ValidInputPortRequest
public record CommentPostInputPortRequest(
        @ValidId(message = "Invalid 'postId'")
        String postId,
        @ValidUsername(message = "Invalid 'username'")
        String username,
        @Size(max = 250, message = "Text is limited to 250 Characters")
        String commentText
){}