package de.hsos.swa.infrastructure.persistence.model;

import de.hsos.swa.domain.entity.Topic;
import de.hsos.swa.domain.vo.Vote;
import de.hsos.swa.domain.entity.Comment;
import de.hsos.swa.domain.entity.Post;
import de.hsos.swa.domain.entity.User;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Entity(name = "Post")
@Table(name = "post_table")
@NamedQuery(name = "PostPersistenceModel.findAll", query = "SELECT p FROM Post p")
@NamedQuery(name = "PostPersistenceModel.findById", query = "SELECT p FROM Post p WHERE p.id = :id")
public class PostPersistenceModel {
    @Id
    UUID id;

    @Basic
    String title;

    @Basic
    String content;

    @ManyToOne
    @JoinColumn(name = "topic_id")
    TopicPersistenceModel topicPersistenceModel;

    @ManyToOne
    @JoinColumn(name = "user_id")
    UserPersistenceModel userPersistenceModel;

    @OneToMany(
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    List<CommentPersistenceModel> comments = new ArrayList<>();

    @OneToMany(
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    List<PostVotePersistenceModel> votes = new ArrayList<>();



    public PostPersistenceModel() {
    }

    public PostPersistenceModel(UUID id, String title, String content, TopicPersistenceModel topicPersistenceModel, UserPersistenceModel userPersistenceModel, List<CommentPersistenceModel> comments, List<PostVotePersistenceModel> votes) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.topicPersistenceModel = topicPersistenceModel;
        this.userPersistenceModel = userPersistenceModel;
        this.comments = comments;
        this.votes = votes;
    }

    public PostPersistenceModel(UUID id, String title, String content, TopicPersistenceModel topicPersistenceModel, UserPersistenceModel userPersistenceModel, List<PostVotePersistenceModel> votes) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.topicPersistenceModel = topicPersistenceModel;
        this.userPersistenceModel = userPersistenceModel;
        this.votes = votes;
    }

    public static class Converter {
        public static Post toDomainEntity(PostPersistenceModel postPersistenceModel) {
            User user = UserPersistenceModel.Converter.toDomainEntity(postPersistenceModel.userPersistenceModel);
            Topic topic = TopicPersistenceModel.Converter.toDomainEntity(postPersistenceModel.topicPersistenceModel);
            List<Comment> comments = postPersistenceModel.comments.stream().map(CommentPersistenceModel.Converter::toDomainEntity).toList();
            List<Vote> votes = postPersistenceModel.votes.stream().map(PostVotePersistenceModel.Converter::toDomainEntity).toList();

            Post post = new Post(postPersistenceModel.id, postPersistenceModel.title, postPersistenceModel.content, topic, user);

            votes.forEach(post::setVote);
            comments.forEach(post::addComment);

            return post;
        }

        public static PostPersistenceModel toPersistenceModel(Post post) {
            UserPersistenceModel userPersistenceModel = UserPersistenceModel.Converter.toPersistenceModel(post.getCreator());
            TopicPersistenceModel topicPersistenceModel = TopicPersistenceModel.Converter.toPersistenceModel(post.getTopic());
            List<CommentPersistenceModel> comments = post.getComments().stream().map(CommentPersistenceModel.Converter::toPersistenceModel).toList();
            Set<PostVotePersistenceModel> votes = post.getVotes().values().stream().map(PostVotePersistenceModel.Converter::toPersistenceModel).collect(Collectors.toSet());
            return new PostPersistenceModel(post.getId(), post.getTitle(), post.getContent(), topicPersistenceModel, userPersistenceModel, comments, votes.stream().toList());
        }
    }
}
