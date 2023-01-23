package de.hsos.swa.application.input.dto.in;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public record ReplyToCommentInputPortRequest(
        @NotEmpty(message = "postId is missing") @Pattern(regexp = "[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}", message = "postId is not valid") String postId,
        @NotEmpty(message = "commentId is missing") @Pattern(regexp = "[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}", message = "commentId is not valid") String commentId,
        @NotEmpty(message = "username is missing") @Size(min = 4, max = 20, message = "username must be between 4 and 20 characters") @Pattern(regexp = "^[a-zA-Z0-9_.-]*$", message = "username format is not valid") String username,
        @NotEmpty(message = "commentText is missing") String commentText
) {}
