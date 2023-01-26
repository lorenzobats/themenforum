package de.hsos.swa.infrastructure.persistence.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import de.hsos.swa.domain.entity.User;
import de.hsos.swa.domain.entity.Vote;
import de.hsos.swa.domain.entity.VoteType;

import javax.persistence.*;

@Entity(name = "CommentVote")
@Table(name = "comment_vote_table")
public class CommentVotePersistenceModel {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonManagedReference
    UserPersistenceModel userPersistenceModel;

    @Enumerated(EnumType.STRING)
    private VoteType voteType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public CommentVotePersistenceModel() {
    }

    public CommentVotePersistenceModel(VoteType voteType, UserPersistenceModel userPersistenceModel) {
        this.voteType = voteType;
        this.userPersistenceModel = userPersistenceModel;
    }

    public static class Converter {
        public static Vote toDomainEntity(CommentVotePersistenceModel commentVotePersistenceModel) {
            User user = UserPersistenceModel.Converter.toDomainEntity(commentVotePersistenceModel.userPersistenceModel);
            Vote vote = new Vote(user, commentVotePersistenceModel.voteType);

            return vote;
        }

        public static CommentVotePersistenceModel toPersistenceModel(Vote vote) {
            UserPersistenceModel userPersistenceModel = UserPersistenceModel.Converter.toPersistenceModel(vote.getUser());
            return new CommentVotePersistenceModel(vote.getVoteType(), userPersistenceModel);
        }
    }
}
