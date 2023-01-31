package de.hsos.swa.application.input.dto.out;

import de.hsos.swa.domain.entity.Vote;
import de.hsos.swa.domain.vo.VotedEntityType;

import java.util.UUID;

public class VoteInputPortDto{
    public final Vote vote;
    public final  VotedEntityType votedEntityType;
    public final UUID votedEntityId;

    public VoteInputPortDto(Vote vote, VotedEntityType votedEntityType, UUID votedEntityId) {
        this.vote = vote;
        this.votedEntityType = votedEntityType;
        this.votedEntityId = votedEntityId;
    }
}
