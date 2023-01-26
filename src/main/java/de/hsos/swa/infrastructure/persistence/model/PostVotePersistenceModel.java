package de.hsos.swa.infrastructure.persistence.model;

import de.hsos.swa.domain.entity.User;
import de.hsos.swa.domain.entity.Vote;
import de.hsos.swa.domain.entity.VoteType;

import javax.persistence.*;

@Entity(name = "PostVote")
@Table(name = "post_vote_table")
public class PostVotePersistenceModel {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    UserPersistenceModel userPersistenceModel;

    @Enumerated(EnumType.STRING)
    private VoteType voteType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public PostVotePersistenceModel() {
    }

    public PostVotePersistenceModel(VoteType voteType, UserPersistenceModel userPersistenceModel) {
        this.voteType = voteType;
        this.userPersistenceModel = userPersistenceModel;
    }

    public static class Converter {
        public static Vote toDomainEntity(PostVotePersistenceModel postVotePersistenceModel) {
            User user = UserPersistenceModel.Converter.toDomainEntity(postVotePersistenceModel.userPersistenceModel);
            Vote vote = new Vote(user, postVotePersistenceModel.voteType);

            return vote;
        }

        public static PostVotePersistenceModel toPersistenceModel(Vote vote) {
            UserPersistenceModel userPersistenceModel = UserPersistenceModel.Converter.toPersistenceModel(vote.getUser());
            return new PostVotePersistenceModel(vote.getVoteType(), userPersistenceModel);
        }
    }
}
