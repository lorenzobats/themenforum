package de.hsos.swa.infrastructure.ui.dto.in;

import javax.validation.constraints.NotEmpty;

public class ReplyToCommentUIRequest {
    @NotEmpty(message = "replyText is missing")
    public String replyText;
}

