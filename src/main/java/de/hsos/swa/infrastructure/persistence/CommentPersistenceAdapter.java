package de.hsos.swa.infrastructure.persistence;

import com.blazebit.persistence.CriteriaBuilder;
import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.view.EntityViewManager;
import com.blazebit.persistence.view.EntityViewSetting;
import de.hsos.swa.application.output.repository.dto.out.RepositoryResult;
import de.hsos.swa.application.output.repository.CommentRepository;
import de.hsos.swa.domain.entity.Comment;
import de.hsos.swa.infrastructure.persistence.view.CommentPersistenceView;
import de.hsos.swa.infrastructure.persistence.model.CommentPersistenceModel;
import org.jboss.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@RequestScoped
@Transactional(value = Transactional.TxType.MANDATORY)
public class CommentPersistenceAdapter implements CommentRepository {
    @Inject
    EntityManager entityManager;

    @Inject
    CriteriaBuilderFactory criteriaBuilderFactory;

    @Inject
    EntityViewManager entityViewManager;

    @Inject
    Logger log;

    // READ
    @Override
    public RepositoryResult<Comment> getCommentById(UUID commentId, boolean includeReplies) {
        try {
            CriteriaBuilder<CommentPersistenceModel> criteriaBuilder = criteriaBuilderFactory.create(entityManager, CommentPersistenceModel.class);
            criteriaBuilder.where("id").eq(commentId);
            if (!includeReplies) {
                CriteriaBuilder<CommentPersistenceView> criteriaBuilderView = entityViewManager.applySetting(EntityViewSetting.create(CommentPersistenceView.class), criteriaBuilder);
                CommentPersistenceView comment = criteriaBuilderView.getSingleResult();
                return RepositoryResult.ok(CommentPersistenceView.toDomainEntity(comment));
            } else {
                CommentPersistenceModel comment = criteriaBuilder.getSingleResult();
                return RepositoryResult.ok(CommentPersistenceModel.Converter.toDomainEntity(comment));
            }
        } catch (NoResultException e) {
            log.error("getCommentById: No Comments Found", e);
            return RepositoryResult.notFound();
        } catch (PersistenceException e) {
            log.error("getCommentById Persistence Failed", e);
            return RepositoryResult.error();
        } catch (Exception e) {
            log.error("getCommentById Error", e);
            return RepositoryResult.error();
        }
    }

    @Override
    public RepositoryResult<List<Comment>> getAllComments(boolean includeReplies) {
        try {
            CriteriaBuilder<CommentPersistenceModel> criteriaBuilder = criteriaBuilderFactory.create(entityManager, CommentPersistenceModel.class);
            return getCommentResultList(includeReplies, criteriaBuilder);
        } catch (NoResultException e) {
            return RepositoryResult.notFound();
        } catch (PersistenceException e) {
            log.error("getAllComments Persistence Failed", e);
            return RepositoryResult.error();
        } catch (Exception e) {
            log.error("getAllComments Error", e);
            return RepositoryResult.error();
        }
    }

    // HILFSMETHODEN
    private RepositoryResult<List<Comment>> getCommentResultList(boolean includeComments, CriteriaBuilder<CommentPersistenceModel> criteriaBuilder) {
        if (!includeComments) {
            List<CommentPersistenceView> commentList;
            CriteriaBuilder<CommentPersistenceView> criteriaBuilderView = entityViewManager.applySetting(EntityViewSetting.create(CommentPersistenceView.class), criteriaBuilder);
            commentList = criteriaBuilderView.getResultList();
            return RepositoryResult.ok(commentList.stream().map(CommentPersistenceView::toDomainEntity).toList());
        }
        List<CommentPersistenceModel> commentList;
        commentList = criteriaBuilder.getResultList();
        return RepositoryResult.ok(commentList.stream().map(CommentPersistenceModel.Converter::toDomainEntity).toList());
    }
}
