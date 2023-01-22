package de.hsos.swa.infrastructure.persistence.post;

import de.hsos.swa.domain.entity.Topic;
import de.hsos.swa.domain.vo.Vote;
import de.hsos.swa.infrastructure.persistence.comment.CommentPersistenceEntity;
import de.hsos.swa.infrastructure.persistence.topic.TopicPersistenceEntity;
import de.hsos.swa.infrastructure.persistence.user.UserPersistenceEntity;
import de.hsos.swa.domain.entity.Comment;
import de.hsos.swa.domain.entity.Post;
import de.hsos.swa.domain.entity.User;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

@Entity(name = "Post")
@Table(name = "post_table")
@NamedQuery(name = "PostPersistenceEntity.findAll", query = "SELECT p FROM Post p")
@NamedQuery(name = "PostPersistenceEntity.findById", query = "SELECT p FROM Post p WHERE p.id = :id")
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

    @OneToMany(
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    List<PostVotePersistenceEntity> votes = new ArrayList<>();



    public PostPersistenceEntity() {
    }

    public PostPersistenceEntity(UUID id, String title, String content, TopicPersistenceEntity topicPersistenceEntity, UserPersistenceEntity userPersistenceEntity, List<CommentPersistenceEntity> comments, List<PostVotePersistenceEntity> votes) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.topicPersistenceEntity = topicPersistenceEntity;
        this.userPersistenceEntity = userPersistenceEntity;
        this.comments = comments;
        this.votes = votes;
    }

    public PostPersistenceEntity(UUID id, String title, String content, TopicPersistenceEntity topicPersistenceEntity, UserPersistenceEntity userPersistenceEntity, List<PostVotePersistenceEntity> votes) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.topicPersistenceEntity = topicPersistenceEntity;
        this.userPersistenceEntity = userPersistenceEntity;
        this.votes = votes;
    }

    public static class Converter {
        public static Post toDomainEntity(PostPersistenceEntity postPersistenceEntity) {
            User user = UserPersistenceEntity.Converter.toDomainEntity(postPersistenceEntity.userPersistenceEntity);
            Topic topic = TopicPersistenceEntity.Converter.toDomainEntity(postPersistenceEntity.topicPersistenceEntity);
            List<Comment> comments = postPersistenceEntity.comments.stream().map(CommentPersistenceEntity.Converter::toDomainEntity).toList();
            List<Vote> votes = postPersistenceEntity.votes.stream().map(PostVotePersistenceEntity.Converter::toDomainEntity).toList();

            Post post = new Post(postPersistenceEntity.id, postPersistenceEntity.title, postPersistenceEntity.content, topic, user);

            votes.forEach(post::setVote);
            comments.forEach(post::addComment);

            return post;
        }

        public static PostPersistenceEntity toPersistenceEntity(Post post) {
            UserPersistenceEntity userPersistenceEntity = UserPersistenceEntity.Converter.toPersistenceEntity(post.getCreator());
            TopicPersistenceEntity topicPersistenceEntity = TopicPersistenceEntity.Converter.toPersistenceEntity(post.getTopic());
            List<CommentPersistenceEntity> comments = post.getComments().stream().map(CommentPersistenceEntity.Converter::toPersistenceEntity).toList();
            Set<PostVotePersistenceEntity> votes = post.getVotes().values().stream().map(PostVotePersistenceEntity.Converter::toPersistenceEntity).collect(Collectors.toSet());
            return new PostPersistenceEntity(post.getId(), post.getTitle(), post.getContent(), topicPersistenceEntity, userPersistenceEntity, comments, votes.stream().toList());
        }
    }
}
