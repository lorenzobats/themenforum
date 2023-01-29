package de.hsos.swa.application.input.dto.in;

import de.hsos.swa.application.input.validation.constraints.ValidUsername;

public record GetVotedCommentsByUserInputPortRequest(
        @ValidUsername String username
) {
}
