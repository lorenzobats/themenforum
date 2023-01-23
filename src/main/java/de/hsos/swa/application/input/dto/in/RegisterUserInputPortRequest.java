package de.hsos.swa.application.input.dto.in;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public record RegisterUserInputPortRequest(
        @NotEmpty(message = "Username empty") @Pattern(regexp = "^[a-zA-Z0-9_.-]*$", message = "Username has wrong format") @Size(min = 4, max = 20, message = "Username must be between 4 and 20 characters") String username,
        @NotEmpty(message = "Password empty") String password
) {}
