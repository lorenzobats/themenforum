package de.hsos.swa.infrastructure.persistence.model;

import de.hsos.swa.domain.entity.Vote;
import de.hsos.swa.domain.entity.Comment;
import de.hsos.swa.domain.entity.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Entity(name = "Comment")
@Table(name = "comment_table")
public class CommentPersistenceModel {
    @Id
    UUID id;

    @Basic
    String text;

    @Basic
    LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    UserPersistenceModel userPersistenceModel;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "parent_comment_id")
    CommentPersistenceModel parentComment;

    @OneToMany(
            mappedBy = "parentComment",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    List<CommentPersistenceModel> replies = new ArrayList<>();

    @OneToMany(
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    @JoinTable(
            name = "comment_vote",
            joinColumns = @JoinColumn(name = "comment_id"),
            inverseJoinColumns = @JoinColumn(name = "vote_id")
    )
    List<VotePersistenceModel> votes = new ArrayList<>();

    @Basic
    boolean active;


    public CommentPersistenceModel() {
    }

    public CommentPersistenceModel(UUID id, String text, LocalDateTime createdAt, UserPersistenceModel userPersistenceModel, CommentPersistenceModel parentComment, List<CommentPersistenceModel> replies, boolean active) {
        this.id = id;
        this.text = text;
        this.createdAt = createdAt;
        this.userPersistenceModel = userPersistenceModel;
        this.parentComment = parentComment;
        this.replies = replies;
        this.active = active;
    }

    public CommentPersistenceModel(UUID id, String text, LocalDateTime createdAt, UserPersistenceModel userPersistenceModel, boolean active) {
        this.id = id;
        this.text = text;
        this.createdAt = createdAt;
        this.userPersistenceModel = userPersistenceModel;
        this.active = active;
    }

    public void setVotes(List<VotePersistenceModel> votes) {
        this.votes = votes;
    }

    public UUID getId() {
        return id;
    }

    public static class Converter {
        public static Comment toDomainEntity(CommentPersistenceModel persistenceModel) {
            User user = UserPersistenceModel.Converter.toDomainEntity(persistenceModel.userPersistenceModel);
            Comment comment = new Comment(persistenceModel.id, persistenceModel.createdAt, user, persistenceModel.text, persistenceModel.active);

            for (CommentPersistenceModel cP : persistenceModel.replies) {
                comment.addReply(CommentPersistenceModel.Converter.toDomainEntity(cP));
            }

            List<Vote> votes = persistenceModel.votes.stream().map(VotePersistenceModel.Converter::toDomainEntity).toList();
            votes.forEach(comment::addVote);

            return comment;
        }

        public static CommentPersistenceModel toPersistenceModel(Comment domainEntity) {
            UserPersistenceModel user = UserPersistenceModel.Converter.toPersistenceModel(domainEntity.getUser());
            List<CommentPersistenceModel> replies =
                    domainEntity.getReplies().stream().map(CommentPersistenceModel.Converter::toPersistenceModel).collect(Collectors.toList());

            List<VotePersistenceModel> votes = domainEntity.getVotes().stream().map(VotePersistenceModel.Converter::toPersistenceModel).toList();

            CommentPersistenceModel parent = null;
            if(domainEntity.getParentComment() != null) {
                 parent = CommentPersistenceModel.Converter.parentToPersistenceModel(domainEntity.getParentComment());
            }

            CommentPersistenceModel commentPersistenceModel = new CommentPersistenceModel(domainEntity.getId(), domainEntity.getText(), domainEntity.getCreatedAt(), user, parent, replies, domainEntity.isActive());
            commentPersistenceModel.setVotes(votes);

            return commentPersistenceModel;
        }


        public static CommentPersistenceModel parentToPersistenceModel(Comment domainEntity) {
            UserPersistenceModel userPersistenceModel = UserPersistenceModel.Converter.toPersistenceModel(domainEntity.getUser());
            List<VotePersistenceModel> votes = domainEntity.getVotes().stream().map(VotePersistenceModel.Converter::toPersistenceModel).toList();

            CommentPersistenceModel commentPersistenceModel = new CommentPersistenceModel(domainEntity.getId(), domainEntity.getText(), domainEntity.getCreatedAt(), userPersistenceModel, domainEntity.isActive());
            commentPersistenceModel.setVotes(votes);
            return  commentPersistenceModel;
        }
    }
}
