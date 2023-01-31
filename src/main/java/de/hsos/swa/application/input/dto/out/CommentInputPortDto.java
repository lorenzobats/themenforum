package de.hsos.swa.application.input.dto.out;

import de.hsos.swa.application.annotations.InputPortDTO;
import de.hsos.swa.domain.entity.Vote;
import de.hsos.swa.domain.vo.VotedEntityType;

import java.util.UUID;
// TODO: in PersistenceView die übergabe von votes bei include Comment = none auch ermoeglichen
@InputPortDTO
public class CommentInputPortDto {
    public final Vote vote;
    public final  VotedEntityType votedEntityType;
    public final UUID votedEntityId;

    public CommentInputPortDto(Vote vote, VotedEntityType votedEntityType, UUID votedEntityId) {
        this.vote = vote;
        this.votedEntityType = votedEntityType;
        this.votedEntityId = votedEntityId;
    }
}
