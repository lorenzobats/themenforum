package de.hsos.swa.infrastructure.persistence.post;

import de.hsos.swa.application.input.Result;
import de.hsos.swa.application.output.persistence.PostRepository;
import de.hsos.swa.domain.entity.Post;
import org.jboss.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.TransactionRequiredException;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@RequestScoped
@Transactional(value = Transactional.TxType.MANDATORY)
public class PostPersistenceAdapter implements PostRepository {
    @Inject
    EntityManager entityManager;

    @Inject
    Logger log;


    @Override
    public Result<List<Post>> getAllPosts(boolean includeComments) {
        TypedQuery<PostPersistenceEntity> query = includeComments
                ? entityManager.createNamedQuery("PostPersistenceEntity.findAll", PostPersistenceEntity.class)
                : entityManager.createNamedQuery("PostPersistenceEntity.findAllExcludeComments", PostPersistenceEntity.class);

        List<PostPersistenceEntity> postList;
        try {
            postList = query.getResultList();
            return Result.success(postList.stream().map(PostPersistenceEntity.Converter::toDomainEntity).toList());
        } catch (Exception e) {
            log.error("GetPostById Error", e);
            return Result.exception(e);
        }
    }


    @Override
    public Result<Post> getPostById(UUID postId, boolean includeComments) {
        TypedQuery<PostPersistenceEntity> query = includeComments
                ? entityManager.createNamedQuery("PostPersistenceEntity.findById", PostPersistenceEntity.class)
                : entityManager.createNamedQuery("PostPersistenceEntity.findByIdExcludeComments", PostPersistenceEntity.class);

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
    public Result<Post> getPostById(UUID postId) {
        return getPostById(postId, true);
    }


    @Override
    public Result<Void> deletePost(String postId) {
        return null;
    }



    @Override
    public Result<Post> savePost(Post post) {
        PostPersistenceEntity postPersistenceEntity = PostPersistenceEntity.Converter.toPersistenceEntity(post);
        try {
            entityManager.persist(postPersistenceEntity);
            return Result.success(PostPersistenceEntity.Converter.toDomainEntity(postPersistenceEntity));
        } catch (EntityExistsException | IllegalArgumentException | TransactionRequiredException e) {
            log.error("savePost Error", e);
            return Result.exception(e);
        }
    }


    @Override
    @Transactional(value = Transactional.TxType.MANDATORY)
    public Result<Post> updatePost(Post post) {
        PostPersistenceEntity postPersistenceEntity = PostPersistenceEntity.Converter.toPersistenceEntity(post);
        try {
            entityManager.merge(postPersistenceEntity);
            return Result.success(PostPersistenceEntity.Converter.toDomainEntity(postPersistenceEntity));
        } catch (EntityExistsException | IllegalArgumentException | TransactionRequiredException e) {
            log.error("updatePost Error", e);
            return Result.exception(e);
        }
    }
}
