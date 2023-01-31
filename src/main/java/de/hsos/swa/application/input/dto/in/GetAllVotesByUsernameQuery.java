package de.hsos.swa.application.input.dto.in;

import de.hsos.swa.application.annotations.InputPortRequest;
import de.hsos.swa.domain.validation.constraints.ValidUsername;

@InputPortRequest
public record GetAllVotesByUsernameQuery(
        @ValidUsername String username
) {}
