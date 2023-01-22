package de.hsos.swa.infrastructure.persistence.post;

import de.hsos.swa.domain.entity.User;
import de.hsos.swa.domain.vo.Vote;
import de.hsos.swa.domain.vo.VoteType;
import de.hsos.swa.infrastructure.persistence.user.UserPersistenceEntity;

import javax.persistence.*;

@Entity(name = "PostVote")
@Table(name = "post_vote_table")
public class PostVotePersistenceEntity {
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


    public PostVotePersistenceEntity() {
    }

    public PostVotePersistenceEntity(VoteType voteType, UserPersistenceEntity userPersistenceEntity) {
        this.voteType = voteType;
        this.userPersistenceEntity = userPersistenceEntity;
    }

    public static class Converter {
        public static Vote toDomainEntity(PostVotePersistenceEntity postVotePersistenceEntity) {
            User user = UserPersistenceEntity.Converter.toDomainEntity(postVotePersistenceEntity.userPersistenceEntity);
            Vote vote = new Vote(user, postVotePersistenceEntity.voteType);

            return vote;
        }

        public static PostVotePersistenceEntity toPersistenceEntity(Vote vote) {
            UserPersistenceEntity userPersistenceEntity = UserPersistenceEntity.Converter.toPersistenceEntity(vote.getUser());
            return new PostVotePersistenceEntity(vote.getVoteType(), userPersistenceEntity);
        }
    }
}
