package de.hsos.swa.application.input.dto.in;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public record DeleteUserInputPortRequest(
        @Pattern(regexp = "[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}", message = "User ID is not valid.") String userId,
        @NotBlank(message = "username is empty") String username
) { }
