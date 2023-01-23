package de.hsos.swa.infrastructure.persistence.model;

import de.hsos.swa.domain.value_object.Vote;
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
    List<PostVotePersistenceModel> votes = new ArrayList<>();


    public CommentPersistenceModel() {
    }

    public CommentPersistenceModel(UUID id, String text, LocalDateTime createdAt, UserPersistenceModel userPersistenceModel, CommentPersistenceModel parentComment, List<CommentPersistenceModel> replies, List<PostVotePersistenceModel> votes) {
        this.id = id;
        this.text = text;
        this.createdAt = createdAt;
        this.userPersistenceModel = userPersistenceModel;
        this.parentComment = parentComment;
        this.replies = replies;
        this.votes = votes;
    }

    public CommentPersistenceModel(UUID id, String text, LocalDateTime createdAt, UserPersistenceModel userPersistenceModel, List<PostVotePersistenceModel> votes) {
        this.id = id;
        this.text = text;
        this.createdAt = createdAt;
        this.userPersistenceModel = userPersistenceModel;
        this.votes = votes;
    }

    public static class Converter {
        public static Comment toDomainEntity(CommentPersistenceModel commentPersistenceModel) {
            User user = UserPersistenceModel.Converter.toDomainEntity(commentPersistenceModel.userPersistenceModel);
            Comment comment = new Comment(commentPersistenceModel.id, commentPersistenceModel.createdAt, user, commentPersistenceModel.text);

            List<Vote> votes = commentPersistenceModel.votes.stream().map(PostVotePersistenceModel.Converter::toDomainEntity).toList();
            votes.forEach(comment::setVote);

            for (CommentPersistenceModel cP : commentPersistenceModel.replies) {
                comment.addReply(CommentPersistenceModel.Converter.toDomainEntity(cP));
            }

            return comment;
        }

        public static CommentPersistenceModel toPersistenceModel(Comment comment) {
            UserPersistenceModel user = UserPersistenceModel.Converter.toPersistenceModel(comment.getUser());
            List<CommentPersistenceModel> replies =
                    comment.getReplies().stream().map(CommentPersistenceModel.Converter::toPersistenceModel).collect(Collectors.toList());

            Set<PostVotePersistenceModel> votes = comment.getVotes().values().stream().map(PostVotePersistenceModel.Converter::toPersistenceModel).collect(Collectors.toSet());

            CommentPersistenceModel parent = null;
            if(comment.getParentComment() != null) {
                 parent = CommentPersistenceModel.Converter.parentToPersistenceModel(comment.getParentComment());
            }

            return new CommentPersistenceModel(comment.getId(), comment.getText(), comment.getCreatedAt(), user, parent, replies, votes.stream().toList());
        }


        public static CommentPersistenceModel parentToPersistenceModel(Comment parentComment) {
            UserPersistenceModel userPersistenceModel = UserPersistenceModel.Converter.toPersistenceModel(parentComment.getUser());
            Set<PostVotePersistenceModel> votes = parentComment.getVotes().values().stream().map(PostVotePersistenceModel.Converter::toPersistenceModel).collect(Collectors.toSet());

            return new CommentPersistenceModel(parentComment.getId(), parentComment.getText(), parentComment.getCreatedAt(), userPersistenceModel, votes.stream().toList());
        }
    }

}
