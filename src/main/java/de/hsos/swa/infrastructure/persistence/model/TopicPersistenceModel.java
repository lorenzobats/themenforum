package de.hsos.swa.infrastructure.persistence.model;

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
    UserPersistenceModel userPersistenceModel;


    @OneToMany(
            mappedBy = "topicPersistenceModel",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true)
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
        public static Topic toDomainEntity(TopicPersistenceModel persistenceModel) {
            User user = UserPersistenceModel.Converter.toDomainEntity(persistenceModel.userPersistenceModel);
            Topic topic = new Topic(persistenceModel.id, persistenceModel.title, persistenceModel.description, persistenceModel.createdAt, user);
            return topic;
        }

        public static TopicPersistenceModel toPersistenceModel(Topic domainEntity) {
            UserPersistenceModel userPersistenceModel = UserPersistenceModel.Converter.toPersistenceModel(domainEntity.getOwner());
            return new TopicPersistenceModel(domainEntity.getId(), domainEntity.getTitle(), domainEntity.getDescription(), domainEntity.getCreatedAt(), userPersistenceModel);
        }
    }
}
