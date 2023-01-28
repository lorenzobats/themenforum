package de.hsos.swa.infrastructure.ui.dto.in;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public record ReplyToCommentUIRequest(
       @NotEmpty(message = "replyText is missing") String replyText
) {}
