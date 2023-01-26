package de.hsos.swa.infrastructure.persistence;

import com.blazebit.persistence.CriteriaBuilder;
import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.view.EntityViewManager;
import com.blazebit.persistence.view.EntityViewSetting;
import de.hsos.swa.application.util.Result;
import de.hsos.swa.application.output.repository.CommentRepository;
import de.hsos.swa.domain.entity.Comment;
import de.hsos.swa.infrastructure.persistence.dto.out.CommentPersistenceView;
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

    @Override
    public Result<Comment> getCommentById(UUID commentId, boolean includeReplies) {
        try {
            CriteriaBuilder<CommentPersistenceModel> criteriaBuilder = criteriaBuilderFactory.create(entityManager, CommentPersistenceModel.class);
            criteriaBuilder.where("id").eq(commentId);
            if (!includeReplies) {
                CriteriaBuilder<CommentPersistenceView> criteriaBuilderView = entityViewManager.applySetting(EntityViewSetting.create(CommentPersistenceView.class), criteriaBuilder);
                CommentPersistenceView comment = criteriaBuilderView.getSingleResult();
                return Result.success(CommentPersistenceView.toDomainEntity(comment));
            } else {
                CommentPersistenceModel comment = criteriaBuilder.getSingleResult();
                return Result.success(CommentPersistenceModel.Converter.toDomainEntity(comment));
            }
        } catch (NoResultException e) {
            log.error("getCommentById: No Comments Found", e);
            return Result.error("getCommentById: No Comments Found");
        } catch (PersistenceException e) {
            log.error("getCommentById Persistence Failed", e);
            return Result.error("getCommentById Persistence Failed");
        } catch (Exception e) {
            log.error("getCommentById Error", e);
            return Result.error("getCommentById Error");
        }
    }

    @Override
    public Result<List<Comment>> getAllComments(boolean includeReplies) {
        try {
            CriteriaBuilder<CommentPersistenceModel> criteriaBuilder = criteriaBuilderFactory.create(entityManager, CommentPersistenceModel.class);
            return getCommentResultList(includeReplies, criteriaBuilder);
        } catch (NoResultException e) {
            return Result.error("getAllFilteredComments Persistence Failed Not Found");
        } catch (PersistenceException e) {
            log.error("getAllFilteredComments Persistence Failed", e);
            return Result.error("getAllFilteredComments Persistence Failed");
        } catch (Exception e) {
            log.error("getAllFilteredComments Error", e);
            return Result.error("getAllFilteredComments Error");
        }
    }

    // HILFSMETHODEN
    private Result<List<Comment>> getCommentResultList(boolean includeComments, CriteriaBuilder<CommentPersistenceModel> criteriaBuilder) {
        if (!includeComments) {
            List<CommentPersistenceView> commentList;
            CriteriaBuilder<CommentPersistenceView> criteriaBuilderView = entityViewManager.applySetting(EntityViewSetting.create(CommentPersistenceView.class), criteriaBuilder);
            commentList = criteriaBuilderView.getResultList();
            return Result.success(commentList.stream().map(CommentPersistenceView::toDomainEntity).toList());
        }
        List<CommentPersistenceModel> commentList;
        commentList = criteriaBuilder.getResultList();
        return Result.success(commentList.stream().map(CommentPersistenceModel.Converter::toDomainEntity).toList());
    }
}
