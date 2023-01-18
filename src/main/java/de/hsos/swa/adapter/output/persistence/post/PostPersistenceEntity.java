package de.hsos.swa.adapter.output.persistence.post;

import de.hsos.swa.adapter.output.persistence.comment.CommentPersistenceEntity;
import de.hsos.swa.adapter.output.persistence.user.UserPersistenceEntity;
import de.hsos.swa.domain.entity.Comment;
import de.hsos.swa.domain.entity.Post;
import de.hsos.swa.domain.entity.User;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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

    public PostPersistenceEntity(UUID id, String title, UserPersistenceEntity userPersistenceEntity, List<CommentPersistenceEntity> comments) {
        this.id = id;
        this.title = title;
        this.userPersistenceEntity = userPersistenceEntity;
        this.comments = comments;
    }


    // TODO: Mapping auf Domain Entities hier ist scheiße
    public static class Converter {
        public static Post toDomainEntity(PostPersistenceEntity postPersistenceEntity) {
            User user = UserPersistenceEntity.Converter.toDomainEntity(postPersistenceEntity.userPersistenceEntity);

            List<Comment> comments = postPersistenceEntity.comments.stream().map(CommentPersistenceEntity.Converter::toDomainEntity).collect(Collectors.toList());
            Post post = new Post(postPersistenceEntity.id, postPersistenceEntity.title, user);
            comments.forEach(post::addComment);
            return post;
        }

        public static PostPersistenceEntity toPersistenceEntity(Post post) {
            //convert user
            UserPersistenceEntity userPersistenceEntity = UserPersistenceEntity.Converter.toPersistenceEntity(post.getUser());

            //convert comments
            List<CommentPersistenceEntity> comments = post.getComments().stream().map(CommentPersistenceEntity.Converter::toPersistenceEntity).collect(Collectors.toList());

            //create new post entity
            return new PostPersistenceEntity(post.getId(), post.getTitle(), userPersistenceEntity, comments);
        }
    }

}
