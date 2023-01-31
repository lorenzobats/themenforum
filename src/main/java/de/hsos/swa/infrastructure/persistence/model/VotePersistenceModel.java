package de.hsos.swa.infrastructure.persistence.model;

import de.hsos.swa.domain.entity.User;
import de.hsos.swa.domain.entity.Vote;
import de.hsos.swa.domain.vo.VoteType;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "Vote")
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

    @ManyToOne
    @JoinTable(
            name = "post_vote",
            joinColumns = @JoinColumn(name = "vote_id"),
            inverseJoinColumns = @JoinColumn(name = "post_id")
    )
    PostPersistenceModel postVote;

    @ManyToOne
    @JoinTable(
            name = "comment_vote",
            joinColumns = @JoinColumn(name = "vote_id"),
            inverseJoinColumns = @JoinColumn(name = "comment_id")
    )
    CommentPersistenceModel commentVote;

    public VotePersistenceModel() {
    }

    public VotePersistenceModel(
            UUID id,
            VoteType voteType,
            UserPersistenceModel userPersistenceModel,
            LocalDateTime createdAt) {
        this.id = id;
        this.voteType = voteType;
        this.userPersistenceModel = userPersistenceModel;
        this.createdAt = createdAt;
    }

    public static class Converter {
        public static Vote toDomainEntity(VotePersistenceModel persistenceModel) {
            User user = UserPersistenceModel.Converter.toDomainEntity(persistenceModel.userPersistenceModel);
            Vote vote = new Vote(persistenceModel.id, user, persistenceModel.voteType, persistenceModel.createdAt);
            return vote;
        }

        public static VotePersistenceModel toPersistenceModel(Vote domainEntity) {
            UserPersistenceModel userPersistenceModel = UserPersistenceModel.Converter.toPersistenceModel(domainEntity.getUser());
            return new VotePersistenceModel(domainEntity.getId(), domainEntity.getVoteType(), userPersistenceModel, domainEntity.getCreatedAt());
        }
    }
}
