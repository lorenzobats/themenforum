package de.hsos.swa.infrastructure.persistence.comment;

import de.hsos.swa.application.Result;
import de.hsos.swa.application.output.persistence.CommentRepository;
import de.hsos.swa.domain.entity.Comment;
import de.hsos.swa.infrastructure.persistence.post.PostPersistenceEntity;
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
        CommentPersistenceEntity commentPersistenceEntity = CommentPersistenceEntity.Converter.toPersistenceEntity(comment);
        try {
            entityManager.merge(commentPersistenceEntity);
            return Result.success(CommentPersistenceEntity.Converter.toDomainEntity(commentPersistenceEntity));
        } catch (EntityExistsException | IllegalArgumentException | TransactionRequiredException e) {
            log.error("updateComment Error", e);
            return Result.exception(e);
        }
    }

    @Override
    public Result<Comment> getCommentById(UUID commentId) {
        TypedQuery<CommentPersistenceEntity> query = entityManager.createNamedQuery("CommentPersistenceEntity.findById", CommentPersistenceEntity.class);
        query.setParameter("id", commentId);
        CommentPersistenceEntity comment;
        try {
            comment = query.getSingleResult();
            return Result.success(CommentPersistenceEntity.Converter.toDomainEntity(comment));
        } catch (Exception e) {
            log.error("GetCommentById Error", e);
            return Result.exception(e);

        }
    }
}
