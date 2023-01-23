package de.hsos.swa.infrastructure.persistence.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import de.hsos.swa.domain.entity.Topic;
import de.hsos.swa.domain.entity.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity(name = "Topic")
@Table(name = "topic_table")
@NamedQuery(name = "TopicPersistenceModel.findAll", query = "SELECT t FROM Topic t")
@NamedQuery(name = "TopicPersistenceModel.findById", query = "SELECT t FROM Topic t WHERE t.id = :id")

public class TopicPersistenceModel {
    @Id
    UUID id;

    @Basic
    String title;

    @Basic
    String description;


    @Basic
    LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonManagedReference
    UserPersistenceModel userPersistenceModel;


    @OneToMany(
            mappedBy = "topicPersistenceModel",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    @JsonBackReference
    List<PostPersistenceModel> posts = new ArrayList<>();

    public TopicPersistenceModel() {
    }

    public TopicPersistenceModel(UUID id, String title, String description, LocalDateTime createdAt, UserPersistenceModel userPersistenceModel) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.createdAt = createdAt;
        this.userPersistenceModel = userPersistenceModel;
    }

    public static class Converter {
        public static Topic toDomainEntity(TopicPersistenceModel topicPersistenceModel) {
            User user = UserPersistenceModel.Converter.toDomainEntity(topicPersistenceModel.userPersistenceModel);
            Topic topic = new Topic(topicPersistenceModel.id, topicPersistenceModel.title, topicPersistenceModel.description, topicPersistenceModel.createdAt, user);
            return topic;
        }

        public static TopicPersistenceModel toPersistenceModel(Topic topic) {
            UserPersistenceModel userPersistenceModel = UserPersistenceModel.Converter.toPersistenceModel(topic.getOwner());
            return new TopicPersistenceModel(topic.getId(), topic.getTitle(), topic.getDescription(), topic.getCreatedAt(), userPersistenceModel);
        }
    }
}
