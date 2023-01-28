package de.hsos.swa.infrastructure.ui.dto.in;

import javax.validation.constraints.NotEmpty;

public record DeletePostUIRequest(
        @NotEmpty(message = "postID is missing") String postID
){}