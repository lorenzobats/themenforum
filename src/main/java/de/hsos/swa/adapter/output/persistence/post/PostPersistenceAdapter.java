package de.hsos.swa.adapter.output.persistence.post;

import de.hsos.swa.application.port.input._shared.Result;
import de.hsos.swa.application.port.output.post.GetPostByIdOutputPort;
import de.hsos.swa.application.port.output.post.SavePostOutputPort;
import de.hsos.swa.application.port.output.post.UpdatePostOutputPort;
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
public class PostPersistenceAdapter implements
        SavePostOutputPort,
        //UpdatePostOutputPort,     // TODO: UpdatePostOutputPort implementieren
        GetPostByIdOutputPort {

    @Inject
    EntityManager entityManager;

    @Inject
    Logger log;

    @Override
    public Result<Post> getPostById(UUID postId) {
        TypedQuery<PostPersistenceEntity> query = entityManager.createNamedQuery("PostPersistenceEntity.findById", PostPersistenceEntity.class);
        query.setParameter("id", postId);
        PostPersistenceEntity post;
        try {
            post = query.getSingleResult();
            return Result.success(PostPersistenceEntity.Converter.toDomainEntity(post));
        } catch (Exception e) {
            log.error("GetPostById Error", e);
            return Result.exception(e);
        }
    }

    @Override
    public Result<UUID> savePost(Post post) {
        PostPersistenceEntity postPersistenceEntity = PostPersistenceEntity.Converter.toPersistenceEntity(post);
        try {
            entityManager.persist(postPersistenceEntity);
            return Result.success(postPersistenceEntity.id);
        } catch (EntityExistsException | IllegalArgumentException | TransactionRequiredException e) {
            log.error("savePost Error", e);
            return Result.exception(e);
        }
    }

//    @Override
//    public Result<Void> updatePost(Post post) {
//        PostPersistenceEntity postPersistenceEntity = PostPersistenceEntity.Converter.toPersistenceEntity(post);
//        try {
//            entityManager.merge(postPersistenceEntity);
//            // TODO: Return generic Result
//            return Result.success();
//        } catch (EntityExistsException | IllegalArgumentException | TransactionRequiredException e) {
//            log.error("savePost Error", e);
//            return Result.exception(e);
//        }
//    }
}
