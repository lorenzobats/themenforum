package de.hsos.swa.actors.ui.dto.in;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import javax.validation.constraints.NotEmpty;

@Schema(hidden = true)
public class CommentPostUIRequest implements UIRequest {
    @NotEmpty(message = "commentText is missing")
    public String commentText;
}