package de.hsos.swa.adapter.output.persistence.comment;

import de.hsos.swa.adapter.output.persistence.post.PostPersistenceEntity;
import de.hsos.swa.adapter.output.persistence.user.UserPersistenceEntity;
import de.hsos.swa.domain.entity.Comment;
import de.hsos.swa.domain.entity.User;

import javax.persistence.*;
import java.util.UUID;

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

    @ManyToOne(fetch = FetchType.LAZY)
    PostPersistenceEntity postPersistenceEntity;

    // TODO: Comments Relation Bidirektional ?
    // https://vladmihalcea.com/the-best-way-to-map-a-onetomany-association-with-jpa-and-hibernate/


    public CommentPersistenceEntity() {
    }

    public CommentPersistenceEntity(UUID id, String text, UserPersistenceEntity userPersistenceEntity) {
        this.id = id;
        this.text = text;
        this.userPersistenceEntity = userPersistenceEntity;
    }


    public static class Converter {
        public static Comment toDomainEntity(CommentPersistenceEntity commentPersistenceEntity) {
            User user = UserPersistenceEntity.Converter.toDomainEntity(commentPersistenceEntity.userPersistenceEntity);
            return new Comment(commentPersistenceEntity.id, user, commentPersistenceEntity.text);
        }

        public static CommentPersistenceEntity toPersistenceEntity(Comment comment) {
            UserPersistenceEntity userPersistenceEntity = UserPersistenceEntity.Converter.toPersistenceEntity(comment.getUser());
            return new CommentPersistenceEntity(comment.getId(), comment.getText(), userPersistenceEntity);
        }
    }

}
