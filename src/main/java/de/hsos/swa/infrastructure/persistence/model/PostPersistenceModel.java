package de.hsos.swa.infrastructure.persistence.model;

import de.hsos.swa.domain.entity.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@Entity(name = "Post")
@Table(name = "post_table")
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
            inverseJoinColumns = @JoinColumn(name = "vote_id"))
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

    public List<CommentPersistenceModel> getComments() {
        return comments;
    }

    public UUID getId() {
        return id;
    }

    public static class Converter {
        public static Post toDomainEntity(PostPersistenceModel persistenceModel) {
            User user = UserPersistenceModel.Converter.toDomainEntity(persistenceModel.userPersistenceModel);
            Topic topic = TopicPersistenceModel.Converter.toDomainEntity(persistenceModel.topicPersistenceModel);
            List<Comment> comments = persistenceModel.comments.stream().map(CommentPersistenceModel.Converter::toDomainEntity).toList();
            List<Vote> votes = persistenceModel.votes.stream().map(VotePersistenceModel.Converter::toDomainEntity).toList();


            Post post = new Post(
                    persistenceModel.id,
                    persistenceModel.title,
                    persistenceModel.content,
                    persistenceModel.createdAt,
                    topic,
                    user);

            comments.forEach(post::add);
            votes.forEach(post::addVote);

            return post;
        }

        public static PostPersistenceModel toPersistenceModel(Post domainEntity) {
            UserPersistenceModel userPersistenceModel = UserPersistenceModel.Converter.toPersistenceModel(domainEntity.getUser());
            TopicPersistenceModel topicPersistenceModel = TopicPersistenceModel.Converter.toPersistenceModel(domainEntity.getTopic());
            List<CommentPersistenceModel> comments = domainEntity.getComments().stream().map(CommentPersistenceModel.Converter::toPersistenceModel).toList();

            PostPersistenceModel postPersistenceModel = new PostPersistenceModel(
                    domainEntity.getId(),
                    domainEntity.getTitle(),
                    domainEntity.getContent(),
                    domainEntity.getCreatedAt(),
                    topicPersistenceModel,
                    userPersistenceModel,
                    comments);

            List<VotePersistenceModel> votes = domainEntity.getVotes().stream().map(VotePersistenceModel.Converter::toPersistenceModel).toList();

            postPersistenceModel.setVotes(votes);

            return postPersistenceModel;
        }
    }
}
