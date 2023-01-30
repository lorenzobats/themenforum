package de.hsos.swa.infrastructure.persistence;

import com.blazebit.persistence.CriteriaBuilder;
import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.view.EntityViewManager;
import com.blazebit.persistence.view.EntityViewSetting;
import de.hsos.swa.application.input.dto.out.TopicWithPostCountDto;
import de.hsos.swa.application.util.Result;
import de.hsos.swa.application.output.repository.TopicRepository;
import de.hsos.swa.domain.entity.Topic;
import de.hsos.swa.infrastructure.persistence.model.TopicPersistenceModel;
import de.hsos.swa.infrastructure.persistence.view.TopicPersistenceView;
import org.jboss.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@RequestScoped
@Transactional(value = Transactional.TxType.MANDATORY)
public class TopicPersistenceAdapter implements TopicRepository {
    @Inject
    EntityManager entityManager;
    @Inject
    CriteriaBuilderFactory criteriaBuilderFactory;

    @Inject
    EntityViewManager entityViewManager;

    @Inject
    Logger log;

    // CREATE
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

    // READ
    @Override
    public Result<List<TopicWithPostCountDto>> getAllTopicsWithPostCount() {
        // TODO: Try Catch
        CriteriaBuilder<TopicPersistenceModel> criteriaBuilder = criteriaBuilderFactory.create(entityManager, TopicPersistenceModel.class);
        List<TopicPersistenceView> postList;
        CriteriaBuilder<TopicPersistenceView> criteriaBuilderView = entityViewManager.applySetting(EntityViewSetting.create(TopicPersistenceView.class), criteriaBuilder);
        postList = criteriaBuilderView.getResultList();
        return Result.success(postList.stream().map(TopicPersistenceView::toDomainEntityWithPostCount).toList());
    }

    @Override
    public Result<List<TopicWithPostCountDto>> searchTopic(String searchString) {
        // TODO: Try Catch
        CriteriaBuilder<TopicPersistenceModel> criteriaBuilder = criteriaBuilderFactory.create(entityManager, TopicPersistenceModel.class);
        criteriaBuilder
                .whereOr()
                .where("title").like().value("%" + searchString + "%").noEscape()
                .where("description").like().value("%" + searchString + "%").noEscape()
                .endOr();
        CriteriaBuilder<TopicPersistenceView> criteriaBuilderView = entityViewManager.applySetting(EntityViewSetting.create(TopicPersistenceView.class), criteriaBuilder);
        List<TopicPersistenceView> postList = criteriaBuilderView.getResultList();
        return Result.success(postList.stream().map(TopicPersistenceView::toDomainEntityWithPostCount).toList());
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

    // DELETE
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
}
