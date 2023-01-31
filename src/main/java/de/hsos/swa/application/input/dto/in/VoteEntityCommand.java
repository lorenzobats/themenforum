package de.hsos.swa.application.input.dto.in;

import de.hsos.swa.application.annotations.InputPortRequest;
import de.hsos.swa.domain.validation.constraints.ValidId;
import de.hsos.swa.domain.validation.constraints.ValidUsername;
import de.hsos.swa.domain.vo.VotedEntityType;
import de.hsos.swa.domain.vo.VoteType;

@InputPortRequest
public record VoteEntityCommand(
        @ValidId String entityId,
        @ValidUsername String username,

        // TODO VoteType Validator
        VoteType voteType,
        VotedEntityType entityType
) { }
