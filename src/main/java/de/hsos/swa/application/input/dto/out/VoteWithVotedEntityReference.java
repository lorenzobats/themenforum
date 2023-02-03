package de.hsos.swa.application.input.dto.out;

import de.hsos.swa.application.annotations.InputPortDTO;
import de.hsos.swa.domain.entity.Vote;
import de.hsos.swa.domain.vo.VotedEntityType;

import java.util.UUID;

@InputPortDTO
public class VoteWithVotedEntityReference {
    public final Vote vote;
    public final  VotedEntityType votedEntityType;
    public final UUID votedEntityId;

    public VoteWithVotedEntityReference(Vote vote, VotedEntityType votedEntityType, UUID votedEntityId) {
        this.vote = vote;
        this.votedEntityType = votedEntityType;
        this.votedEntityId = votedEntityId;
    }
}
