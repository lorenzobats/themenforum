package de.hsos.swa.application.input.dto.in;

import de.hsos.swa.application.annotations.InputPortRequest;
import de.hsos.swa.application.input.validation.constraints.ValidId;
import de.hsos.swa.domain.vo.VotedEntityType;
import de.hsos.swa.domain.vo.VoteType;

import javax.validation.constraints.NotNull;

@InputPortRequest
public record VoteEntityCommand(
        @ValidId String entityId,
        @NotNull VoteType voteType,
        @NotNull VotedEntityType entityType
) {}
