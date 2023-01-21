package de.hsos.swa.infrastructure.persistence.topic;

import de.hsos.swa.domain.entity.Topic;
import de.hsos.swa.domain.entity.User;
import de.hsos.swa.infrastructure.persistence.post.PostPersistenceEntity;
import de.hsos.swa.infrastructure.persistence.user.UserPersistenceEntity;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity(name = "Topic")
@Table(name = "topic_table")
@NamedQuery(name = "TopicPersistenceEntity.findAll", query = "SELECT t FROM Topic t")
@NamedQuery(name = "TopicPersistenceEntity.findById", query = "SELECT t FROM Topic t WHERE t.id = :id")

public class TopicPersistenceEntity {
    @Id
    UUID id;

    @Basic
    String title;

    @Basic
    String description;

    // TODO: Date
    //@Basic
    //LocalDate createdAt;

    @ManyToOne
    UserPersistenceEntity userPersistenceEntity;

    public TopicPersistenceEntity() {
    }

    public TopicPersistenceEntity(UUID id, String title, String description, UserPersistenceEntity userPersistenceEntity) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.userPersistenceEntity = userPersistenceEntity;
    }

    public TopicPersistenceEntity(UUID id, String title, String description, UserPersistenceEntity userPersistenceEntity, List<PostPersistenceEntity> posts) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.userPersistenceEntity = userPersistenceEntity;
    }

    public static class Converter {
        public static Topic toDomainEntity(TopicPersistenceEntity topicPersistenceEntity) {
            User user = UserPersistenceEntity.Converter.toDomainEntity(topicPersistenceEntity.userPersistenceEntity);
            Topic topic = new Topic(topicPersistenceEntity.id, topicPersistenceEntity.title, topicPersistenceEntity.description, user);
            return topic;
        }

        public static TopicPersistenceEntity toPersistenceEntity(Topic topic) {
            UserPersistenceEntity userPersistenceEntity = UserPersistenceEntity.Converter.toPersistenceEntity(topic.getOwner());
            return new TopicPersistenceEntity(topic.getId(), topic.getTitle(), topic.getDescription(), userPersistenceEntity);
        }
    }
}
