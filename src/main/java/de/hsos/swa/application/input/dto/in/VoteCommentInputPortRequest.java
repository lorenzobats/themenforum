package de.hsos.swa.application.input.dto.in;

import de.hsos.swa.application.input.validation.constraints.ValidId;
import de.hsos.swa.application.input.validation.constraints.ValidInputPortRequest;
import de.hsos.swa.application.input.validation.constraints.ValidUsername;
import de.hsos.swa.domain.entity.VoteType;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@ValidInputPortRequest
public record VoteCommentInputPortRequest(
       @ValidId String commentId,
        @ValidUsername String username,
       // TODO VoteType Validator
       VoteType voteType
) {
}
