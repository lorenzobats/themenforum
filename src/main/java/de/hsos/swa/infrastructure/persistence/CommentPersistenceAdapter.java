package de.hsos.swa.infrastructure.persistence;

import com.blazebit.persistence.CriteriaBuilder;
import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.view.EntityViewManager;
import com.blazebit.persistence.view.EntityViewSetting;
import de.hsos.swa.application.util.Result;
import de.hsos.swa.application.output.repository.CommentRepository;
import de.hsos.swa.domain.entity.Comment;
import de.hsos.swa.domain.entity.Post;
import de.hsos.swa.infrastructure.persistence.dto.out.CommentPersistenceView;
import de.hsos.swa.infrastructure.persistence.dto.out.PostPersistenceView;
import de.hsos.swa.infrastructure.persistence.model.CommentPersistenceModel;
import de.hsos.swa.infrastructure.persistence.model.PostPersistenceModel;
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

    @Override
    public Result<Comment> getCommentById(UUID commentId, boolean includeReplies) {
        try {
            CriteriaBuilder<CommentPersistenceModel> criteriaBuilder = criteriaBuilderFactory.create(entityManager, CommentPersistenceModel.class);
            criteriaBuilder.where("id").eq(commentId);
            if (!includeReplies) {
                CriteriaBuilder<CommentPersistenceView> criteriaBuilderView = entityViewManager.applySetting(EntityViewSetting.create(CommentPersistenceView.class), criteriaBuilder);
                CommentPersistenceView comment = criteriaBuilderView.getSingleResult();
                return Result.isSuccessful(CommentPersistenceView.toDomainEntity(comment));
            } else {
                CommentPersistenceModel comment = criteriaBuilder.getSingleResult();
                return Result.isSuccessful(CommentPersistenceModel.Converter.toDomainEntity(comment));
            }
        } catch (NoResultException e) {
            log.error("getCommentById: No Comments Found", e);
            return Result.notFound();
        } catch (PersistenceException e) {
            log.error("getCommentById Persistence Failed", e);
            return Result.exception();
        } catch (Exception e) {
            log.error("getCommentById Error", e);
            return Result.exception();
        }
    }

    @Override
    public Result<List<Comment>> getAllComments(boolean includeReplies) {
        try {
            CriteriaBuilder<CommentPersistenceModel> criteriaBuilder = criteriaBuilderFactory.create(entityManager, CommentPersistenceModel.class);
            return getCommentResultList(includeReplies, criteriaBuilder);
        } catch (NoResultException e) {
            return Result.notFound();
        } catch (PersistenceException e) {
            log.error("getAllFilteredComments Persistence Failed", e);
            return Result.exception();
        } catch (Exception e) {
            log.error("getAllFilteredComments Error", e);
            return Result.exception();
        }
    }

    // HILFSMETHODEN
    private Result<List<Comment>> getCommentResultList(boolean includeComments, CriteriaBuilder<CommentPersistenceModel> criteriaBuilder) {
        if (!includeComments) {
            List<CommentPersistenceView> commentList;
            CriteriaBuilder<CommentPersistenceView> criteriaBuilderView = entityViewManager.applySetting(EntityViewSetting.create(CommentPersistenceView.class), criteriaBuilder);
            commentList = criteriaBuilderView.getResultList();
            return Result.isSuccessful(commentList.stream().map(CommentPersistenceView::toDomainEntity).toList());
        }
        List<CommentPersistenceModel> commentList;
        commentList = criteriaBuilder.getResultList();
        return Result.isSuccessful(commentList.stream().map(CommentPersistenceModel.Converter::toDomainEntity).toList());
    }
}
