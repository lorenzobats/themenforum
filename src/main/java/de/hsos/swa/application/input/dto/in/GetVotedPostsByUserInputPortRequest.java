package de.hsos.swa.application.input.dto.in;

import javax.validation.constraints.NotBlank;

public record GetVotedPostsByUserInputPortRequest(
        @NotBlank(message = "username is empty") String username
) {
}
