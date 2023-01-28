package de.hsos.swa.infrastructure.ui.dto.in;

import javax.validation.constraints.NotEmpty;

public record CommentPostUIRequest(
        @NotEmpty(message = "commentText is missing") String commentText
){}