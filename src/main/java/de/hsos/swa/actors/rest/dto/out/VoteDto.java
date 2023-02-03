package de.hsos.swa.actors.rest.dto.out;

import de.hsos.swa.application.input.dto.out.VoteWithVotedEntityReference;
import de.hsos.swa.domain.entity.Vote;
import de.hsos.swa.domain.vo.VoteType;
import de.hsos.swa.domain.vo.VotedEntityType;

import java.time.LocalDateTime;
import java.util.UUID;

public class VoteDto {
    public String id;
    public String user;
    public VoteType voteType;
    public LocalDateTime createdAt;

    public VotedEntityType votedEntityType;
    public UUID votedEntityId;


    public VoteDto(String id, String user, VoteType voteType, LocalDateTime createdAt, VotedEntityType votedEntityType, UUID votedEntityId) {
        this.id = id;
        this.user = user;
        this.voteType = voteType;
        this.createdAt = createdAt;
        this.votedEntityType = votedEntityType;
        this.votedEntityId = votedEntityId;
    }

    public VoteDto(String id, String user, VoteType voteType, LocalDateTime createdAt) {
        this.id = id;
        this.user = user;
        this.voteType = voteType;
        this.createdAt = createdAt;
    }

    public static class Converter {
        public static VoteDto fromInputPortDto(VoteWithVotedEntityReference inputPortDto) {
            return new VoteDto(
                    inputPortDto.vote.getId().toString(),
                    inputPortDto.vote.getUser().getName(),
                    inputPortDto.vote.getVoteType(),
                    inputPortDto.vote.getCreatedAt(),
                    inputPortDto.votedEntityType,
                    inputPortDto.votedEntityId);
        }

        public static VoteDto fromDomainEntity(Vote vote) {
            return new VoteDto(
                    vote.getId().toString(),
                    vote.getUser().getName(),
                    vote.getVoteType(),
                    vote.getCreatedAt());
        }

    }
}

