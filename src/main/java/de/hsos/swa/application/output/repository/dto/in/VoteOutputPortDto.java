package de.hsos.swa.application.output.repository.dto.in;

import de.hsos.swa.domain.entity.Vote;
import de.hsos.swa.domain.vo.VotedEntityType;

import java.util.UUID;

public record VoteOutputPortDto(
    Vote vote,
    VotedEntityType votedEntityType,
    UUID votedEntityId
) {}
