package de.hsos.swa.application.input.dto.in;

import de.hsos.swa.application.annotations.InputPortRequest;
import de.hsos.swa.application.input.validation.constraints.ValidPassword;
import de.hsos.swa.application.input.validation.constraints.ValidUsername;
@InputPortRequest
public record RegisterUserCommand(
        @ValidUsername String username,
        @ValidPassword String password
) {}
