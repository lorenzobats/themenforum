package de.hsos.swa.infrastructure.persistence;

import com.blazebit.persistence.CriteriaBuilder;
import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.view.EntityViewManager;
import com.blazebit.persistence.view.EntityViewSetting;
import de.hsos.swa.application.input.dto.out.TopicWithPostCountDto;
import de.hsos.swa.application.output.repository.TopicRepository;
import de.hsos.swa.application.output.repository.dto.out.RepositoryResult;
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

/**
 *
 */
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

    //------------------------------------------------------------------------------------------------------------------
    // COMMANDS
    @Override
    public RepositoryResult<Topic> saveTopic(Topic topic) {
        TopicPersistenceModel topicPersistenceModel = TopicPersistenceModel.Converter.toPersistenceModel(topic);
        try {
            entityManager.persist(topicPersistenceModel);
            return RepositoryResult.ok(TopicPersistenceModel.Converter.toDomainEntity(topicPersistenceModel));
        } catch (IllegalArgumentException e) {
            log.warn(e);
            return RepositoryResult.exception();
        } catch (PersistenceException e) {
            log.error(e);
            return RepositoryResult.exception();
        }
    }

    @Override
    public RepositoryResult<Topic> updateTopic(Topic topic) {
        TopicPersistenceModel topicPersistenceModel = TopicPersistenceModel.Converter.toPersistenceModel(topic);
        try {
            entityManager.merge(topicPersistenceModel);
            return RepositoryResult.ok(TopicPersistenceModel.Converter.toDomainEntity(topicPersistenceModel));
        } catch (IllegalArgumentException e) {
            log.warn(e);
            return RepositoryResult.exception();
        } catch (PersistenceException e) {
            log.error(e);
            return RepositoryResult.exception();
        }
    }

    @Override
    public RepositoryResult<Topic> deleteTopic(UUID topicId) {
        try {
            TopicPersistenceModel topic = entityManager.find(TopicPersistenceModel.class, topicId);
            if (topic != null) {
                entityManager.remove(topic);
                return RepositoryResult.ok(TopicPersistenceModel.Converter.toDomainEntity(topic));
            }
            return RepositoryResult.notFound();
        } catch (NoResultException e) {
            return RepositoryResult.notFound();
        } catch (IllegalArgumentException e) {
            log.warn(e);
            return RepositoryResult.exception();
        } catch (PersistenceException e) {
            log.error(e);
            return RepositoryResult.exception();
        }
    }

    //------------------------------------------------------------------------------------------------------------------
    // QUERIES
    @Override
    public RepositoryResult<List<TopicWithPostCountDto>> getAllTopics() {
        try {
            CriteriaBuilder<TopicPersistenceModel> criteriaBuilder = criteriaBuilderFactory.create(entityManager, TopicPersistenceModel.class);
            CriteriaBuilder<TopicPersistenceView> criteriaBuilderView = entityViewManager.applySetting(EntityViewSetting.create(TopicPersistenceView.class), criteriaBuilder);
            List<TopicPersistenceView> postList = criteriaBuilderView.getResultList();
            return RepositoryResult.ok(postList.stream().map(TopicPersistenceView::toOutputPortDto).toList());
        } catch (NoResultException e) {
            return RepositoryResult.notFound();
        } catch (IllegalArgumentException e) {
            log.warn(e);
            return RepositoryResult.exception();
        } catch (PersistenceException e) {
            log.error(e);
            return RepositoryResult.exception();
        }
    }

    @Override
    public RepositoryResult<Topic> getTopicById(UUID topicId) {
        try {
            CriteriaBuilder<TopicPersistenceModel> criteriaBuilder = criteriaBuilderFactory.create(entityManager, TopicPersistenceModel.class);
            criteriaBuilder.where("id").eq(topicId);
            TopicPersistenceModel post = criteriaBuilder.getSingleResult();
            return RepositoryResult.ok(TopicPersistenceModel.Converter.toDomainEntity(post));
        } catch (NoResultException e) {
            return RepositoryResult.notFound();
        } catch (IllegalArgumentException e) {
            log.warn(e);
            return RepositoryResult.exception();
        } catch (PersistenceException e) {
            log.error(e);
            return RepositoryResult.exception();
        }
    }

    @Override
    public RepositoryResult<List<TopicWithPostCountDto>> searchTopic(String searchString) {
        try {
            CriteriaBuilder<TopicPersistenceModel> criteriaBuilder = criteriaBuilderFactory.create(entityManager, TopicPersistenceModel.class);
            criteriaBuilder
                    .whereOr()
                    .where("title").like().value("%" + searchString + "%").noEscape()
                    .where("description").like().value("%" + searchString + "%").noEscape()
                    .where("userPersistenceModel.name").eq().value(searchString)
                    .endOr();
            CriteriaBuilder<TopicPersistenceView> criteriaBuilderView = entityViewManager.applySetting(EntityViewSetting.create(TopicPersistenceView.class), criteriaBuilder);
            List<TopicPersistenceView> postList = criteriaBuilderView.getResultList();
            return RepositoryResult.ok(postList.stream().map(TopicPersistenceView::toOutputPortDto).toList());
        } catch (NoResultException e) {
            return RepositoryResult.notFound();
        } catch (IllegalArgumentException e) {
            log.warn(e);
            return RepositoryResult.exception();
        } catch (PersistenceException e) {
            log.error(e);
            return RepositoryResult.exception();
        }
    }
}
