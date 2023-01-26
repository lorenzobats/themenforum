package de.hsos.swa.infrastructure.persistence;

import de.hsos.swa.application.util.Result;
import de.hsos.swa.application.output.repository.TopicRepository;
import de.hsos.swa.domain.entity.Topic;
import de.hsos.swa.infrastructure.persistence.model.TopicPersistenceModel;
import org.jboss.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        TypedQuery<TopicPersistenceModel> query = entityManager.createNamedQuery("TopicPersistenceModel.findAll", TopicPersistenceModel.class);

        List<TopicPersistenceModel> topicList;
        try {
            topicList = query.getResultList();
            return Result.success(topicList.stream().map(TopicPersistenceModel.Converter::toDomainEntity).toList());
        } catch (Exception e) {
            log.error("GetAllTopics Error", e);
            return Result.error("GetAllTopics Error");
        }
    }

    public Result<Map<UUID, Long>> getPostCountForAllTopics() {
        String queryString = "SELECT t.id AS topic_id, COUNT(p) as post_count FROM Topic t LEFT JOIN Post p ON t = p.topicPersistenceModel GROUP BY t.id";
        TypedQuery<Tuple> query = entityManager.createQuery(queryString, Tuple.class);
        List<Tuple> resultList = query.getResultList();
        Map<UUID, Long> resultMap = new HashMap<>();
        for (Tuple tuple : resultList) {
            resultMap.put((UUID) tuple.get("topic_id"), (Long) tuple.get("post_count"));
        }
        return Result.success(resultMap);
    }

    @Override
    public Result<Topic> deleteTopic(UUID topicId) {
        try {
            TopicPersistenceModel post = entityManager.find(TopicPersistenceModel.class, topicId);
            if (post != null) {
                entityManager.remove(post);
                return Result.success(TopicPersistenceModel.Converter.toDomainEntity(post));
            }
            return Result.error("");
        } catch (EntityExistsException | IllegalArgumentException | TransactionRequiredException e) {
            log.error("Delete Error", e);
            return Result.error("Delete Error");
        }
    }

    @Override
    public Result<Topic> saveTopic(Topic topic) {
        TopicPersistenceModel topicPersistenceModel = TopicPersistenceModel.Converter.toPersistenceModel(topic);
        try {
            entityManager.persist(topicPersistenceModel);
            return Result.success(TopicPersistenceModel.Converter.toDomainEntity(topicPersistenceModel));
        } catch (EntityExistsException | IllegalArgumentException | TransactionRequiredException e) {
            log.error("saveTopic Error", e);
            return Result.error("saveTopic Error");
        }
    }

    @Override
    public Result<Topic> getTopicById(UUID topicId) {
        TypedQuery<TopicPersistenceModel> query = entityManager.createNamedQuery("TopicPersistenceModel.findById", TopicPersistenceModel.class);

        query.setParameter("id", topicId);
        TopicPersistenceModel topic;
        try {
            topic = query.getSingleResult();
            return Result.success(TopicPersistenceModel.Converter.toDomainEntity(topic));
        } catch (Exception e) {
            log.error("GetTopicById Error", e);
            return Result.error("GetTopicById Error");
        }
    }
}
