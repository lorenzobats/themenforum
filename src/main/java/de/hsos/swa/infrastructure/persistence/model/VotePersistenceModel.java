package de.hsos.swa.infrastructure.persistence.model;

import de.hsos.swa.domain.entity.User;
import de.hsos.swa.domain.entity.Vote;
import de.hsos.swa.domain.entity.VoteType;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "PostVote")
@Table(name = "vote_table")
public class VotePersistenceModel {
    @Id
    UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    UserPersistenceModel userPersistenceModel;

    @Enumerated(EnumType.STRING)
    VoteType voteType;

    @Basic
    LocalDateTime createdAt;


    public VotePersistenceModel() {
    }

    public VotePersistenceModel(UUID id, VoteType voteType, UserPersistenceModel userPersistenceModel, LocalDateTime createdAt) {
        this.id = id;
        this.voteType = voteType;
        this.userPersistenceModel = userPersistenceModel;
        this.createdAt = createdAt;
    }

    public static class Converter {
        public static Vote toDomainEntity(VotePersistenceModel votePersistenceModel) {
            User user = UserPersistenceModel.Converter.toDomainEntity(votePersistenceModel.userPersistenceModel);
            Vote vote = new Vote(votePersistenceModel.id, user, votePersistenceModel.voteType, votePersistenceModel.createdAt);

            return vote;
        }

        public static VotePersistenceModel toPersistenceModel(Vote vote) {
            UserPersistenceModel userPersistenceModel = UserPersistenceModel.Converter.toPersistenceModel(vote.getUser());
            return new VotePersistenceModel(vote.getId(), vote.getVoteType(), userPersistenceModel, vote.getCreatedAt());
        }
    }
}
