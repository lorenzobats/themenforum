package de.hsos.swa.adapter.output.persistence.comment;

import de.hsos.swa.adapter.output.persistence.post.PostPersistenceEntity;
import de.hsos.swa.application.port.input._shared.Result;
import de.hsos.swa.application.port.output.CommentRepository;
import de.hsos.swa.domain.entity.Comment;
import de.hsos.swa.domain.entity.Post;
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
    public Result<UUID> saveComment(Comment comment, Post post) {
        CommentPersistenceEntity commentPersistenceEntity = CommentPersistenceEntity.Converter.toPersistenceEntity(comment);
        try {
            entityManager.persist(commentPersistenceEntity);
            return Result.success(commentPersistenceEntity.id);
        } catch (EntityExistsException | IllegalArgumentException | TransactionRequiredException e) {
            log.error("savePost Error", e);
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
