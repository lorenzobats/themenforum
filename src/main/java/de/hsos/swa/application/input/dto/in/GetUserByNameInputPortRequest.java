package de.hsos.swa.application.input.dto.in;

import de.hsos.swa.application.input.validation.constraints.ValidUsername;

public record GetUserByNameInputPortRequest(
        @ValidUsername String username
) {}
