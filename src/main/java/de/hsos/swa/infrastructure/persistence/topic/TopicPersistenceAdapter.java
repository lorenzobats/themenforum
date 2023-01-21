package de.hsos.swa.infrastructure.persistence.topic;

import de.hsos.swa.application.Result;
import de.hsos.swa.application.output.persistence.TopicRepository;
import de.hsos.swa.domain.entity.Topic;
import de.hsos.swa.infrastructure.persistence.post.PostPersistenceEntity;
import org.jboss.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.TransactionRequiredException;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@RequestScoped
@Transactional(value = Transactional.TxType.MANDATORY)
public class TopicPersistenceAdapter implements TopicRepository {
    @Inject
    EntityManager entityManager;

    @Inject
    Logger log;

    @Override
    public Result<List<Topic>> getAllTopics() {
        TypedQuery<TopicPersistenceEntity> query = entityManager.createNamedQuery("TopicPersistenceEntity.findAll", TopicPersistenceEntity.class);

        List<TopicPersistenceEntity> topicList;
        try {
            topicList = query.getResultList();
            return Result.success(topicList.stream().map(TopicPersistenceEntity.Converter::toDomainEntity).toList());
        } catch (Exception e) {
            log.error("GetAllTopics Error", e);
            return Result.exception(e);
        }
    }

    @Override
    public Result<Topic> saveTopic(Topic topic) {
        TopicPersistenceEntity topicPersistenceEntity = TopicPersistenceEntity.Converter.toPersistenceEntity(topic);
        try {
            entityManager.persist(topicPersistenceEntity);
            return Result.success(TopicPersistenceEntity.Converter.toDomainEntity(topicPersistenceEntity));
        } catch (EntityExistsException | IllegalArgumentException | TransactionRequiredException e) {
            log.error("saveTopic Error", e);
            return Result.exception(e);
        }
    }

    @Override
    public Result<Topic> getTopicById(UUID topicId) {
        TypedQuery<TopicPersistenceEntity> query = entityManager.createNamedQuery("TopicPersistenceEntity.findById", TopicPersistenceEntity.class);

        query.setParameter("id", topicId);
        TopicPersistenceEntity topic;
        try {
            topic = query.getSingleResult();
            return Result.success(TopicPersistenceEntity.Converter.toDomainEntity(topic));
        } catch (Exception e) {
            log.error("GetTopicById Error", e);
            return Result.exception(e);
        }
    }
}
