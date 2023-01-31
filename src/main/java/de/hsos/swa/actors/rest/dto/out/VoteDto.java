package de.hsos.swa.actors.rest.dto.out;

import de.hsos.swa.application.input.dto.out.VoteInputPortDto;
import de.hsos.swa.domain.entity.Vote;
import de.hsos.swa.domain.vo.VotedEntityType;

import java.util.UUID;

public class VoteDto {

    public Vote vote;
    public VotedEntityType votedEntityType;
    public UUID votedEntityId;


    public VoteDto(Vote vote, VotedEntityType votedEntityType, UUID votedEntityId) {
        this.vote = vote;
        this.votedEntityType = votedEntityType;
        this.votedEntityId = votedEntityId;
    }


    public static VoteDto fromInputPortDto(VoteInputPortDto inputPortDto) {
            return new VoteDto(inputPortDto.vote, inputPortDto.votedEntityType, inputPortDto.votedEntityId);
    }
}