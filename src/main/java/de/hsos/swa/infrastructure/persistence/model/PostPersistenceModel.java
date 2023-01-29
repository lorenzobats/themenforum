package de.hsos.swa.infrastructure.persistence.model;

import de.hsos.swa.domain.entity.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

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

    @Basic
    LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "topic_id")
    TopicPersistenceModel topicPersistenceModel;

    @ManyToOne
    @JoinColumn(name = "user_id")
    UserPersistenceModel userPersistenceModel;

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true)

    List<CommentPersistenceModel> comments = new ArrayList<>();


    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    @JoinTable(
            name = "post_vote",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "vote_id")
    )
    List<VotePersistenceModel> votes = new ArrayList<>();

    public PostPersistenceModel() {
    }

    public PostPersistenceModel(
            UUID id,
            String title,
            String content,
            LocalDateTime createdAt,
            TopicPersistenceModel topicPersistenceModel,
            UserPersistenceModel userPersistenceModel,
            List<CommentPersistenceModel> comments) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.topicPersistenceModel = topicPersistenceModel;
        this.userPersistenceModel = userPersistenceModel;
        this.comments = comments;
    }

    public void setVotes(List<VotePersistenceModel> votes) {
        this.votes = votes;
    }

    public static class Converter {
        public static Post toDomainEntity(PostPersistenceModel postPersistenceModel) {
            User user = UserPersistenceModel.Converter.toDomainEntity(postPersistenceModel.userPersistenceModel);
            Topic topic = TopicPersistenceModel.Converter.toDomainEntity(postPersistenceModel.topicPersistenceModel);
            List<Comment> comments = postPersistenceModel.comments.stream().map(CommentPersistenceModel.Converter::toDomainEntity).toList();
            List<Vote> votes = postPersistenceModel.votes.stream().map(VotePersistenceModel.Converter::toDomainEntity).toList();


            Post post = new Post(
                    postPersistenceModel.id,
                    postPersistenceModel.title,
                    postPersistenceModel.content,
                    postPersistenceModel.createdAt,
                    topic,
                    user);

            comments.forEach(post::addComment);
            votes.forEach(post::addVote);

            return post;
        }

        public static PostPersistenceModel toPersistenceModel(Post post) {
            UserPersistenceModel userPersistenceModel = UserPersistenceModel.Converter.toPersistenceModel(post.getCreator());
            TopicPersistenceModel topicPersistenceModel = TopicPersistenceModel.Converter.toPersistenceModel(post.getTopic());
            List<CommentPersistenceModel> comments = post.getComments().stream().map(CommentPersistenceModel.Converter::toPersistenceModel).toList();

            PostPersistenceModel postPersistenceModel = new PostPersistenceModel(
                    post.getId(),
                    post.getTitle(),
                    post.getContent(),
                    post.getCreatedAt(),
                    topicPersistenceModel,
                    userPersistenceModel,
                    comments);

            List<VotePersistenceModel> votes = post.getVotes().stream().map(VotePersistenceModel.Converter::toPersistenceModel).toList();

            postPersistenceModel.setVotes(votes);

            return postPersistenceModel;
        }
    }
}
