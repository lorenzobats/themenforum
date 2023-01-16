package de.hsos.swa.adapter.output.persistence.post;

import de.hsos.swa.adapter.output.persistence.comment.CommentPersistenceEntity;
import de.hsos.swa.adapter.output.persistence.user.UserPersistenceEntity;
import de.hsos.swa.domain.entity.Post;
import de.hsos.swa.domain.entity.User;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity(name = "Post")
@Table(name = "post_table")
@NamedQuery(name = "PostPersistenceEntity.findById", query = "SELECT p FROM Post p WHERE p.id = :id")
public class PostPersistenceEntity {
    @Id
    UUID id;

    @Basic
    String title;

    @ManyToOne
    UserPersistenceEntity userPersistenceEntity;

    // TODO: Comments Relation
    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    List<CommentPersistenceEntity> comments = new ArrayList<>();


    public PostPersistenceEntity() {
    }

    public PostPersistenceEntity(UUID id, String title, UserPersistenceEntity userPersistenceEntity) {
        this.id = id;
        this.title = title;
        this.userPersistenceEntity = userPersistenceEntity;
    }


    // TODO: Mapping auf Domain Entities hier ist scheiße
    public static class Converter {
        public static Post toDomainEntity(PostPersistenceEntity postPersistenceEntity) {
            User user = UserPersistenceEntity.Converter.toDomainEntity(postPersistenceEntity.userPersistenceEntity);
            // TODO: Konstruktor ohne Id verstecken
            return new Post(postPersistenceEntity.id, postPersistenceEntity.title, user);
        }

        public static PostPersistenceEntity toPersistenceEntity(Post post) {
            UserPersistenceEntity userPersistenceEntity = UserPersistenceEntity.Converter.toPersistenceEntity(post.getUser());
            return new PostPersistenceEntity(post.getId(), post.getTitle(), userPersistenceEntity);
        }
    }

}
