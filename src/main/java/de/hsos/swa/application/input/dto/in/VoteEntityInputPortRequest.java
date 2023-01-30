package de.hsos.swa.application.input.dto.in;

import de.hsos.swa.application.input.validation.constraints.ValidId;
import de.hsos.swa.application.input.validation.constraints.ValidInputPortRequest;
import de.hsos.swa.application.input.validation.constraints.ValidUsername;
import de.hsos.swa.domain.vo.VotedEntityType;
import de.hsos.swa.domain.vo.VoteType;

@ValidInputPortRequest
public record VoteEntityInputPortRequest(
        @ValidId String entityId,
        @ValidUsername String username,

        // TODO VoteType Validator
        VoteType voteType,
        VotedEntityType entityType
) { }
