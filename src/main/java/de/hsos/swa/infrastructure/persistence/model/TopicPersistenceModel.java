package de.hsos.swa.infrastructure.persistence.model;

import de.hsos.swa.domain.entity.Topic;
import de.hsos.swa.domain.entity.User;

import javax.persistence.*;
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

    @ManyToOne
    @JoinColumn(name = "user_id")
    UserPersistenceModel userPersistenceModel;


    // TODO: Date
    //@Basic
    //LocalDate createdAt;

    public TopicPersistenceModel() {
    }

    public TopicPersistenceModel(UUID id, String title, String description, UserPersistenceModel userPersistenceModel) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.userPersistenceModel = userPersistenceModel;
    }

    public static class Converter {
        public static Topic toDomainEntity(TopicPersistenceModel topicPersistenceModel) {
            User user = UserPersistenceModel.Converter.toDomainEntity(topicPersistenceModel.userPersistenceModel);
            Topic topic = new Topic(topicPersistenceModel.id, topicPersistenceModel.title, topicPersistenceModel.description, user);
            return topic;
        }

        public static TopicPersistenceModel toPersistenceModel(Topic topic) {
            UserPersistenceModel userPersistenceModel = UserPersistenceModel.Converter.toPersistenceModel(topic.getOwner());
            return new TopicPersistenceModel(topic.getId(), topic.getTitle(), topic.getDescription(), userPersistenceModel);
        }
    }
}
