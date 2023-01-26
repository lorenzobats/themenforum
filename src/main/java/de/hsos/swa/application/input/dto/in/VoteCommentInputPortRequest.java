package de.hsos.swa.application.input.dto.in;

import de.hsos.swa.domain.entity.VoteType;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public record VoteCommentInputPortRequest(
        @NotEmpty(message = "commentId is missing") String commentId,
        @NotEmpty(message = "username is missing") String username,
        @NotNull(message = "voteType is missing") VoteType voteType
) {
}
