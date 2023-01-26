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
@NamedQuery(name = "CommentPersistenceModel.findById", query = "SELECT c FROM Comment c WHERE c.id = :id")
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

    public static class Converter {
        public static Comment toDomainEntity(CommentPersistenceModel commentPersistenceModel) {
            User user = UserPersistenceModel.Converter.toDomainEntity(commentPersistenceModel.userPersistenceModel);
            Comment comment = new Comment(commentPersistenceModel.id, commentPersistenceModel.createdAt, user, commentPersistenceModel.text, commentPersistenceModel.active);

            for (CommentPersistenceModel cP : commentPersistenceModel.replies) {
                comment.addReply(CommentPersistenceModel.Converter.toDomainEntity(cP));
            }

            List<Vote> votes = commentPersistenceModel.votes.stream().map(VotePersistenceModel.Converter::toDomainEntity).toList();
            votes.forEach(comment::addVote);

            return comment;
        }

        public static CommentPersistenceModel toPersistenceModel(Comment comment) {
            UserPersistenceModel user = UserPersistenceModel.Converter.toPersistenceModel(comment.getUser());
            List<CommentPersistenceModel> replies =
                    comment.getReplies().stream().map(CommentPersistenceModel.Converter::toPersistenceModel).collect(Collectors.toList());

            List<VotePersistenceModel> votes = comment.getVotes().stream().map(VotePersistenceModel.Converter::toPersistenceModel).toList();

            CommentPersistenceModel parent = null;
            if(comment.getParentComment() != null) {
                 parent = CommentPersistenceModel.Converter.parentToPersistenceModel(comment.getParentComment());
            }

            CommentPersistenceModel commentPersistenceModel = new CommentPersistenceModel(comment.getId(), comment.getText(), comment.getCreatedAt(), user, parent, replies, comment.isActive());
            commentPersistenceModel.setVotes(votes);

            return commentPersistenceModel;
        }


        public static CommentPersistenceModel parentToPersistenceModel(Comment parentComment) {
            UserPersistenceModel userPersistenceModel = UserPersistenceModel.Converter.toPersistenceModel(parentComment.getUser());
            List<VotePersistenceModel> votes = parentComment.getVotes().stream().map(VotePersistenceModel.Converter::toPersistenceModel).toList();

            CommentPersistenceModel commentPersistenceModel = new CommentPersistenceModel(parentComment.getId(), parentComment.getText(), parentComment.getCreatedAt(), userPersistenceModel, parentComment.isActive());
            return  commentPersistenceModel;
        }
    }
}
