package de.hsos.swa.application.input.dto.in;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public record CommentPostInputPortRequest(
        @NotEmpty(message = "postId is missing") String postId,
        @NotEmpty(message = "userId is missing") String username,
        @NotEmpty(message = "Text is missing") @Size(max = 250, message = "Text is limited to 250 Characters") String commentText
){}