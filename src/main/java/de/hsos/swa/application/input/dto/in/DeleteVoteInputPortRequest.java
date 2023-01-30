package de.hsos.swa.application.input.dto.in;

import de.hsos.swa.application.input.validation.constraints.ValidId;
import de.hsos.swa.application.input.validation.constraints.ValidUsername;

public record DeleteVoteInputPortRequest(
        @ValidId String vote,
        @ValidUsername String username) { }
