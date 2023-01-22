package de.hsos.swa.infrastructure.persistence.comment;

import de.hsos.swa.domain.vo.Vote;
import de.hsos.swa.infrastructure.persistence.post.PostVotePersistenceEntity;
import de.hsos.swa.infrastructure.persistence.user.UserPersistenceEntity;
import de.hsos.swa.domain.entity.Comment;
import de.hsos.swa.domain.entity.User;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

// https://vladmihalcea.com/the-best-way-to-map-a-onetomany-association-with-jpa-and-hibernate/
@Entity(name = "Comment")
@Table(name = "comment_table")
@NamedQuery(name = "CommentPersistenceEntity.findById", query = "SELECT c FROM Comment c WHERE c.id = :id")
public class CommentPersistenceEntity {
    @Id
    UUID id;

    @Basic
    String text;

    @ManyToOne
    UserPersistenceEntity userPersistenceEntity;

    @ManyToOne
    CommentPersistenceEntity parentComment;

    @OneToMany(
            mappedBy = "parentComment",
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER)
    List<CommentPersistenceEntity> replies = new ArrayList<>();

    @OneToMany(
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    List<PostVotePersistenceEntity> votes = new ArrayList<>();


    public CommentPersistenceEntity() {
    }

    public CommentPersistenceEntity(UUID id, String text, UserPersistenceEntity userPersistenceEntity, CommentPersistenceEntity parentComment, List<CommentPersistenceEntity> replies, List<PostVotePersistenceEntity> votes) {
        this.id = id;
        this.text = text;
        this.userPersistenceEntity = userPersistenceEntity;
        this.parentComment = parentComment;
        this.replies = replies;
        this.votes = votes;
    }

    public CommentPersistenceEntity(UUID id, String text, UserPersistenceEntity userPersistenceEntity, List<PostVotePersistenceEntity> votes) {
        this.id = id;
        this.text = text;
        this.userPersistenceEntity = userPersistenceEntity;
        this.votes = votes;
    }

    public static class Converter {
        public static Comment toDomainEntity(CommentPersistenceEntity commentPersistenceEntity) {
            User user = UserPersistenceEntity.Converter.toDomainEntity(commentPersistenceEntity.userPersistenceEntity);
            Comment comment = new Comment(commentPersistenceEntity.id, user, commentPersistenceEntity.text);

            List<Vote> votes = commentPersistenceEntity.votes.stream().map(PostVotePersistenceEntity.Converter::toDomainEntity).toList();
            votes.forEach(comment::setVote);

            for (CommentPersistenceEntity cP : commentPersistenceEntity.replies) {
                comment.addReply(CommentPersistenceEntity.Converter.toDomainEntity(cP));
            }

            return comment;
        }

        public static CommentPersistenceEntity toPersistenceEntity(Comment comment) {
            UserPersistenceEntity user = UserPersistenceEntity.Converter.toPersistenceEntity(comment.getUser());
            List<CommentPersistenceEntity> replies =
                    comment.getReplies().stream().map(CommentPersistenceEntity.Converter::toPersistenceEntity).collect(Collectors.toList());

            Set<PostVotePersistenceEntity> votes = comment.getVotes().values().stream().map(PostVotePersistenceEntity.Converter::toPersistenceEntity).collect(Collectors.toSet());

            CommentPersistenceEntity parent = null;
            if(comment.getParentComment() != null) {
                 parent = CommentPersistenceEntity.Converter.parentToPersistenceEntity(comment.getParentComment());
            }

            return new CommentPersistenceEntity(comment.getId(), comment.getText(), user, parent, replies, votes.stream().toList());
        }


        public static CommentPersistenceEntity parentToPersistenceEntity(Comment parentComment) {
            UserPersistenceEntity userPersistenceEntity = UserPersistenceEntity.Converter.toPersistenceEntity(parentComment.getUser());
            Set<PostVotePersistenceEntity> votes = parentComment.getVotes().values().stream().map(PostVotePersistenceEntity.Converter::toPersistenceEntity).collect(Collectors.toSet());

            return new CommentPersistenceEntity(parentComment.getId(), parentComment.getText(), userPersistenceEntity, votes.stream().toList());
        }
    }

}
