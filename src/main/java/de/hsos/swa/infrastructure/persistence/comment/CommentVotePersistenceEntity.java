package de.hsos.swa.infrastructure.persistence.comment;

import de.hsos.swa.domain.entity.User;
import de.hsos.swa.domain.vo.Vote;
import de.hsos.swa.domain.vo.VoteType;
import de.hsos.swa.infrastructure.persistence.user.UserPersistenceEntity;

import javax.persistence.*;

@Entity(name = "CommentVote")
@Table(name = "comment_vote_table")
public class CommentVotePersistenceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    UserPersistenceEntity userPersistenceEntity;

    @Enumerated(EnumType.STRING)
    private VoteType voteType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public CommentVotePersistenceEntity() {
    }

    public CommentVotePersistenceEntity(VoteType voteType, UserPersistenceEntity userPersistenceEntity) {
        this.voteType = voteType;
        this.userPersistenceEntity = userPersistenceEntity;
    }

    public static class Converter {
        public static Vote toDomainEntity(CommentVotePersistenceEntity commentVotePersistenceEntity) {
            User user = UserPersistenceEntity.Converter.toDomainEntity(commentVotePersistenceEntity.userPersistenceEntity);
            Vote vote = new Vote(user, commentVotePersistenceEntity.voteType);

            return vote;
        }

        public static CommentVotePersistenceEntity toPersistenceEntity(Vote vote) {
            UserPersistenceEntity userPersistenceEntity = UserPersistenceEntity.Converter.toPersistenceEntity(vote.getUser());
            return new CommentVotePersistenceEntity(vote.getVoteType(), userPersistenceEntity);
        }
    }
}
