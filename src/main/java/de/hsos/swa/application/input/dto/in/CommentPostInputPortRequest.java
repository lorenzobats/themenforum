package de.hsos.swa.application.input.dto.in;

import javax.validation.constraints.NotEmpty;

public record CommentPostInputPortRequest(
        @NotEmpty(message = "postId is missing") String postId,
        @NotEmpty(message = "userId is missing") String username,
        @NotEmpty(message = "Text is missing") String commentText
){}