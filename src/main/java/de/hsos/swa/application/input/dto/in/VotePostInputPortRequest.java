package de.hsos.swa.application.input.dto.in;

import de.hsos.swa.domain.value_object.VoteType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public record VotePostInputPortRequest(
        @NotBlank(message = "postId is missing") String postId,
        @NotBlank(message = "username is missing") String username,
        @NotNull(message = "voteType is missing") VoteType voteType
){}
