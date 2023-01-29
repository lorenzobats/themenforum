package de.hsos.swa.infrastructure.ui.dto.in;

import javax.validation.constraints.NotEmpty;

public class CommentPostUIRequest implements UIRequest {
    @NotEmpty(message = "commentText is missing")
    public String commentText;
}