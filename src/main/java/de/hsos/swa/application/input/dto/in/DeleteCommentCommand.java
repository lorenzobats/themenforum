package de.hsos.swa.application.input.dto.in;

import de.hsos.swa.application.annotations.InputPortRequest;
import de.hsos.swa.application.input.validation.constraints.ValidId;
import de.hsos.swa.application.input.validation.constraints.ValidUsername;

@InputPortRequest
public record DeleteCommentCommand(
        @ValidId String commentId,
        @ValidUsername String username
) { }
