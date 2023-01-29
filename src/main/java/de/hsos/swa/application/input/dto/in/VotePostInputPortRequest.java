package de.hsos.swa.application.input.dto.in;

import de.hsos.swa.application.input.validation.constraints.ValidId;
import de.hsos.swa.application.input.validation.constraints.ValidInputPortRequest;
import de.hsos.swa.application.input.validation.constraints.ValidUsername;
import de.hsos.swa.domain.entity.VoteType;

@ValidInputPortRequest
public record VotePostInputPortRequest(
        @ValidId String postId,
        @ValidUsername String username,
        // TODO: VoteType implementieren
        VoteType voteType
){}
