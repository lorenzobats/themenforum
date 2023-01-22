package de.hsos.swa.infrastructure.persistence.post;

import de.hsos.swa.application.Result;
import de.hsos.swa.application.output.persistence.PostRepository;
import de.hsos.swa.domain.entity.Post;
import org.jboss.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.*;
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
                : entityManager.createQuery("SELECT p FROM Post p WHERE p.comments is empty", PostPersistenceEntity.class);

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
                : entityManager.createQuery("SELECT NEW Post(p.id, p.title, p.content, p.topicPersistenceEntity, p.userPersistenceEntity, p.votes) FROM Post p WHERE p.id = :id", PostPersistenceEntity.class);
        // TODO: das funktioniert nicht
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
