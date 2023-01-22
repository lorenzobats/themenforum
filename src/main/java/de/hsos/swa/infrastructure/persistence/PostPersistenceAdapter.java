package de.hsos.swa.infrastructure.persistence;

import de.hsos.swa.application.queries.PostFilterParams;
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
import java.util.Map;
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
    public Result<List<Post>> getAllFilteredPosts(Map<PostFilterParams, Object> filterParams, boolean includeComments) {

        String queryString = "SELECT p FROM Post p" +
                (includeComments ? " WHERE 1=1" : " WHERE p.comments is empty") +   // TODO: Das hier ist noch falsch. Gibt nur die Posts zurück, die keine Kommentare haben.
                (filterParams.containsKey(PostFilterParams.USERNAME) ? " AND p.userPersistenceModel.name = :username" : "") +
                (filterParams.containsKey(PostFilterParams.USERID) ? " AND p.userPersistenceModel.id = :userId" : "") +
                (filterParams.containsKey(PostFilterParams.DATE_FROM) ? " AND p.createdAt >= :dateFrom" : "") +
                (filterParams.containsKey(PostFilterParams.DATE_TO) ? " AND p.createdAt <= :dateTo" : "") +
                (filterParams.containsKey(PostFilterParams.SORT_BY) ? " ORDER BY p." + filterParams.get(PostFilterParams.SORT_BY) : "") +
                (filterParams.containsKey(PostFilterParams.SORT_ORDER) ? " " + filterParams.get(PostFilterParams.SORT_ORDER) : "");

        TypedQuery<PostPersistenceModel> query = entityManager.createQuery(queryString, PostPersistenceModel.class);


        if(filterParams.containsKey(PostFilterParams.USERNAME))
            query.setParameter("username", filterParams.get(PostFilterParams.USERNAME));
        if(filterParams.containsKey(PostFilterParams.USERID))
            query.setParameter("userId", filterParams.get(PostFilterParams.USERID));
        if(filterParams.containsKey(PostFilterParams.DATE_FROM))
            query.setParameter("dateFrom", filterParams.get(PostFilterParams.DATE_FROM));
        if(filterParams.containsKey(PostFilterParams.DATE_TO))
            query.setParameter("dateTo", filterParams.get(PostFilterParams.DATE_TO));


        List<PostPersistenceModel> postList;
        try {
            postList = query.getResultList();
            return Result.success(postList.stream().map(PostPersistenceModel.Converter::toDomainEntity).toList());
        } catch (Exception e) {
            log.error("getAllFilteredPosts Error", e);
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
