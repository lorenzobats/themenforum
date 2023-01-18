package de.hsos.swa.adapter.output.persistence.comment;

import com.fasterxml.jackson.annotation.JsonBackReference;
import de.hsos.swa.adapter.output.persistence.user.UserPersistenceEntity;
import de.hsos.swa.domain.entity.Comment;
import de.hsos.swa.domain.entity.User;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

// TODO: Comments Relation Bidirektional ?
// https://vladmihalcea.com/the-best-way-to-map-a-onetomany-association-with-jpa-and-hibernate/
@Entity(name = "Comment")
@Table(name = "comment_table")
@NamedQuery(name = "CommentPersistenceEntity.findById", query = "SELECT p FROM Comment p WHERE p.id = :id")
public class CommentPersistenceEntity {
    @Id
    UUID id;

    @Basic
    String text;

    @ManyToOne
    UserPersistenceEntity userPersistenceEntity;

    @ManyToOne
    CommentPersistenceEntity parentComment;

    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    List<CommentPersistenceEntity> replies = new ArrayList<>();


    public CommentPersistenceEntity() {
    }

    public CommentPersistenceEntity(
            UUID id,
            String text,
            UserPersistenceEntity userPersistenceEntity,
            CommentPersistenceEntity parent,
            List<CommentPersistenceEntity> replies) {
        this.id = id;
        this.text = text;
        this.userPersistenceEntity = userPersistenceEntity;
        this.parentComment = parent;
        this.replies = replies;
    }

    public CommentPersistenceEntity(
            UUID id,
            String text,
            UserPersistenceEntity userPersistenceEntity) {
        this.id = id;
        this.text = text;
        this.userPersistenceEntity = userPersistenceEntity;
    }


    public static class Converter {
        public static Comment toDomainEntity(CommentPersistenceEntity commentPersistenceEntity) {
            User user = UserPersistenceEntity.Converter.toDomainEntity(commentPersistenceEntity.userPersistenceEntity);
            Comment comment = new Comment(commentPersistenceEntity.id, user, commentPersistenceEntity.text);

            for (CommentPersistenceEntity cP : commentPersistenceEntity.replies) {
                comment.addReply(CommentPersistenceEntity.Converter.toDomainEntity(cP));
            }
            return comment;
        }

        public static CommentPersistenceEntity toPersistenceEntity(Comment comment) {
            UserPersistenceEntity userPersistenceEntity = UserPersistenceEntity.Converter.toPersistenceEntity(comment.getUser());
            List<CommentPersistenceEntity> repliesPersistenceEntity =
                    comment.getReplies().stream().map(CommentPersistenceEntity.Converter::toPersistenceEntity).collect(Collectors.toList());

            CommentPersistenceEntity parent = null;
            if(comment.getParentComment() != null) {
                 parent = CommentPersistenceEntity.Converter.parentToPersistenceEntity(comment.getParentComment());
            }

            return new CommentPersistenceEntity(comment.getId(), comment.getText(), userPersistenceEntity, parent, repliesPersistenceEntity);
        }

        public static CommentPersistenceEntity parentToPersistenceEntity(Comment parentComment) {
            UserPersistenceEntity userPersistenceEntity = UserPersistenceEntity.Converter.toPersistenceEntity(parentComment.getUser());

            return new CommentPersistenceEntity(parentComment.getId(), parentComment.getText(), userPersistenceEntity);
        }
    }

}
