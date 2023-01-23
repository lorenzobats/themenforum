package de.hsos.swa.application.input.dto.in;

import javax.validation.constraints.NotEmpty;

public record GetUserByNameInputPortRequest(
        @NotEmpty(message = "username empty") String username
) {}
