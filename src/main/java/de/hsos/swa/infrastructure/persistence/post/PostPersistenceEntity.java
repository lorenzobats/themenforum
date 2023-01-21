package de.hsos.swa.infrastructure.persistence.post;

import de.hsos.swa.domain.entity.Topic;
import de.hsos.swa.infrastructure.persistence.comment.CommentPersistenceEntity;
import de.hsos.swa.infrastructure.persistence.topic.TopicPersistenceEntity;
import de.hsos.swa.infrastructure.persistence.user.UserPersistenceEntity;
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
@NamedQuery(name = "PostPersistenceEntity.findAll", query = "SELECT p FROM Post p")
@NamedQuery(name = "PostPersistenceEntity.findAllExcludeComments", query = "SELECT NEW Post(p.id, p.title, p.content, p.topicPersistenceEntity, p.userPersistenceEntity) FROM Post p")
@NamedQuery(name = "PostPersistenceEntity.findById", query = "SELECT p FROM Post p WHERE p.id = :id")
@NamedQuery(name = "PostPersistenceEntity.findByIdExcludeComments", query = "SELECT NEW Post(p.id, p.title, p.content, p.topicPersistenceEntity, p.userPersistenceEntity) FROM Post p WHERE p.id = :id")

public class PostPersistenceEntity {
    @Id
    UUID id;

    @Basic
    String title;

    @Basic
    String content;

    @ManyToOne
    TopicPersistenceEntity topicPersistenceEntity;

    @ManyToOne
    UserPersistenceEntity userPersistenceEntity;

    @OneToMany(
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    List<CommentPersistenceEntity> comments = new ArrayList<>();


    public PostPersistenceEntity() {
    }

    public PostPersistenceEntity(UUID id, String title, String content, TopicPersistenceEntity topicPersistenceEntity, UserPersistenceEntity userPersistenceEntity, List<CommentPersistenceEntity> comments) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.topicPersistenceEntity = topicPersistenceEntity;
        this.userPersistenceEntity = userPersistenceEntity;
        this.comments = comments;
    }

    // Wird in Named Queries "...ExcludeComments" verwendet
    public PostPersistenceEntity(UUID id, String title, String content, TopicPersistenceEntity topicPersistenceEntity, UserPersistenceEntity userPersistenceEntity) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.topicPersistenceEntity = topicPersistenceEntity;
        this.userPersistenceEntity = userPersistenceEntity;
    }

    public static class Converter {
        public static Post toDomainEntity(PostPersistenceEntity postPersistenceEntity) {
            User user = UserPersistenceEntity.Converter.toDomainEntity(postPersistenceEntity.userPersistenceEntity);
            Topic topic = TopicPersistenceEntity.Converter.toDomainEntity(postPersistenceEntity.topicPersistenceEntity);
            List<Comment> comments = postPersistenceEntity.comments.stream().map(CommentPersistenceEntity.Converter::toDomainEntity).toList();

            Post post = new Post(postPersistenceEntity.id, postPersistenceEntity.title, postPersistenceEntity.content, topic, user);
            comments.forEach(post::addComment);

            return post;
        }

        public static PostPersistenceEntity toPersistenceEntity(Post post) {
            UserPersistenceEntity userPersistenceEntity = UserPersistenceEntity.Converter.toPersistenceEntity(post.getCreator());
            TopicPersistenceEntity topicPersistenceEntity = TopicPersistenceEntity.Converter.toPersistenceEntity(post.getTopic());
            List<CommentPersistenceEntity> comments = post.getComments().stream().map(CommentPersistenceEntity.Converter::toPersistenceEntity).collect(Collectors.toList());

            return new PostPersistenceEntity(post.getId(), post.getTitle(), post.getContent(), topicPersistenceEntity, userPersistenceEntity, comments);
        }
    }
}
