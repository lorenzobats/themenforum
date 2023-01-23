package de.hsos.swa.infrastructure.persistence;

import de.hsos.swa.application.util.Result;
import de.hsos.swa.application.output.repository.CommentRepository;
import de.hsos.swa.domain.entity.Comment;
import de.hsos.swa.infrastructure.persistence.model.CommentPersistenceModel;
import org.jboss.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.TransactionRequiredException;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.UUID;

@RequestScoped
@Transactional(value = Transactional.TxType.MANDATORY)
public class CommentPersistenceAdapter implements CommentRepository {
    @Inject
    EntityManager entityManager;

    @Inject
    Logger log;

    @Override
    public Result<Comment> updateComment(Comment comment) {
        CommentPersistenceModel commentPersistenceModel = CommentPersistenceModel.Converter.toPersistenceModel(comment);
        try {
            entityManager.merge(commentPersistenceModel);
            return Result.success(CommentPersistenceModel.Converter.toDomainEntity(commentPersistenceModel));
        } catch (EntityExistsException | IllegalArgumentException | TransactionRequiredException e) {
            log.error("updateComment Error", e);
            return Result.exception(e);
        }
    }

    @Override
    public Result<Comment> getCommentById(UUID commentId) {
        TypedQuery<CommentPersistenceModel> query = entityManager.createNamedQuery("CommentPersistenceModel.findById", CommentPersistenceModel.class);
        query.setParameter("id", commentId);
        CommentPersistenceModel comment;
        try {
            comment = query.getSingleResult();
            return Result.success(CommentPersistenceModel.Converter.toDomainEntity(comment));
        } catch (Exception e) {
            log.error("GetCommentById Error", e);
            return Result.exception(e);

        }
    }
}
