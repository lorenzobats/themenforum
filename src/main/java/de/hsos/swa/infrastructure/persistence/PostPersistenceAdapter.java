package de.hsos.swa.infrastructure.persistence;

import de.hsos.swa.application.output.Result;
import de.hsos.swa.application.output.repository.PostRepository;
import de.hsos.swa.domain.entity.Post;
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
public class PostPersistenceAdapter implements PostRepository {
    @Inject
    EntityManager entityManager;

    @Inject
    Logger log;


    @Override
    public Result<List<Post>> getAllPosts(boolean includeComments) {
        TypedQuery<PostPersistenceModel> query = includeComments
                ? entityManager.createNamedQuery("PostPersistenceModel.findAll", PostPersistenceModel.class)
                : entityManager.createQuery("SELECT p FROM Post p WHERE p.comments is empty", PostPersistenceModel.class);

        List<PostPersistenceModel> postList;
        try {
            postList = query.getResultList();
            return Result.success(postList.stream().map(PostPersistenceModel.Converter::toDomainEntity).toList());
        } catch (Exception e) {
            log.error("GetPostById Error", e);
            return Result.exception(e);
        }
    }


    @Override
    public Result<Post> getPostById(UUID postId, boolean includeComments) {
        TypedQuery<PostPersistenceModel> query = includeComments
                ? entityManager.createNamedQuery("PostPersistenceModel.findById", PostPersistenceModel.class)
                : entityManager.createQuery("SELECT NEW Post(p.id, p.title, p.content, p.topicPersistenceModel, p.userPersistenceModel, p.votes) FROM Post p WHERE p.id = :id", PostPersistenceModel.class);
        // TODO: das funktioniert nicht
        query.setParameter("id", postId);
        PostPersistenceModel post;
        try {
            post = query.getSingleResult();
            return Result.success(PostPersistenceModel.Converter.toDomainEntity(post));
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
        PostPersistenceModel postPersistenceModel = PostPersistenceModel.Converter.toPersistenceModel(post);
        try {
            entityManager.persist(postPersistenceModel);
            return Result.success(PostPersistenceModel.Converter.toDomainEntity(postPersistenceModel));
        } catch (EntityExistsException | IllegalArgumentException | TransactionRequiredException e) {
            log.error("savePost Error", e);
            return Result.exception(e);
        }
    }


    @Override
    @Transactional(value = Transactional.TxType.MANDATORY)
    public Result<Post> updatePost(Post post) {
        PostPersistenceModel postPersistenceModel = PostPersistenceModel.Converter.toPersistenceModel(post);
        try {
            entityManager.merge(postPersistenceModel);
            return Result.success(PostPersistenceModel.Converter.toDomainEntity(postPersistenceModel));
        } catch (EntityExistsException | IllegalArgumentException | TransactionRequiredException e) {
            log.error("updatePost Error", e);
            return Result.exception(e);
        }
    }
}
