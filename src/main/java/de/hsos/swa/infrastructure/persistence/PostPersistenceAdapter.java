package de.hsos.swa.infrastructure.persistence;

import com.blazebit.persistence.CriteriaBuilder;
import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.view.EntityViewManager;
import com.blazebit.persistence.view.EntityViewSetting;
import de.hsos.swa.application.use_case_query.PostFilterParams;
import de.hsos.swa.application.util.Result;
import de.hsos.swa.application.output.repository.PostRepository;
import de.hsos.swa.domain.entity.Post;
import de.hsos.swa.infrastructure.persistence.model.CommentPersistenceModel;
import de.hsos.swa.infrastructure.persistence.model.PostPersistenceModel;
import de.hsos.swa.infrastructure.persistence.dto.out.PostPersistenceView;
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
    CriteriaBuilderFactory criteriaBuilderFactory;

    @Inject
    EntityViewManager entityViewManager;


    @Inject
    Logger log;

    @Override
    public Result<List<Post>> getAllPosts(boolean includeComments) {
        try {
            CriteriaBuilder<PostPersistenceModel> criteriaBuilder = criteriaBuilderFactory.create(entityManager, PostPersistenceModel.class);
            return getPostResultList(includeComments, criteriaBuilder);
        } catch (NoResultException e) {
            return Result.error("getAllFilteredPosts Persistence Failed No Posts found");
        } catch (PersistenceException e) {
            log.error("getAllFilteredPosts Persistence Failed", e);
            return Result.error("getAllFilteredPosts Persistence Failed");
        } catch (Exception e) {
            log.error("getAllFilteredPosts Error", e);
            return Result.error("getAllFilteredPosts Error");
        }
    }

    @Override
    public Result<List<Post>> getAllFilteredPosts(Map<PostFilterParams, Object> filterParams, boolean includeComments) {
        try {
            CriteriaBuilder<PostPersistenceModel> criteriaBuilder = criteriaBuilderFactory.create(entityManager, PostPersistenceModel.class);

            if (filterParams.containsKey(PostFilterParams.USERNAME))
                criteriaBuilder.where("userPersistenceModel.name").eq(filterParams.get(PostFilterParams.USERNAME));

            if (filterParams.containsKey(PostFilterParams.USERID))
                criteriaBuilder.where("userPersistenceModel.commentId").eq(filterParams.get(PostFilterParams.USERID));

            if (filterParams.containsKey(PostFilterParams.TOPIC))
                criteriaBuilder.where("topicPersistenceModel.title").eq(filterParams.get(PostFilterParams.TOPIC));

            if (filterParams.containsKey(PostFilterParams.DATE_FROM))
                criteriaBuilder.where("createdAt").ge(filterParams.get(PostFilterParams.DATE_FROM));

            if (filterParams.containsKey(PostFilterParams.DATE_TO))
                criteriaBuilder.where("createdAt").le(filterParams.get(PostFilterParams.DATE_TO));

            return getPostResultList(includeComments, criteriaBuilder);
        } catch (NoResultException e) {
            return Result.error("getPostById: No Posts Found");
        } catch (PersistenceException e) {
            log.error("getAllFilteredPosts Persistence Failed", e);
            return Result.error("getAllFilteredPosts Persistence Failed");
        } catch (Exception e) {
            log.error("getAllFilteredPosts Error", e);
            return Result.error("getAllFilteredPosts Error");
        }
    }

    @Override
    public Result<Post> getPostById(UUID postId, boolean includeComments) {
        try {
            CriteriaBuilder<PostPersistenceModel> criteriaBuilder = criteriaBuilderFactory.create(entityManager, PostPersistenceModel.class);
            criteriaBuilder.where("id").eq(postId);
            if (!includeComments) {
                CriteriaBuilder<PostPersistenceView> criteriaBuilderView = entityViewManager.applySetting(EntityViewSetting.create(PostPersistenceView.class), criteriaBuilder);
                PostPersistenceView post = criteriaBuilderView.getSingleResult();
                return Result.success(PostPersistenceView.toDomainEntity(post));
            } else {
                PostPersistenceModel post = criteriaBuilder.getSingleResult();
                return Result.success(PostPersistenceModel.Converter.toDomainEntity(post));
            }
        } catch (NoResultException e) {
            log.error("getPostById: No Posts Found", e);
            return Result.error("getPostById: No Posts Found");
        } catch (PersistenceException e) {
            log.error("getPostById Persistence Failed", e);
            return Result.error("getPostById Persistence Failed");
        } catch (Exception e) {
            log.error("getPostById Error", e);
            return Result.error("getPostById Error");
        }
    }

    @Override
    public Result<Post> getPostByCommentId(UUID commentId) {
        try {
            CriteriaBuilder<PostPersistenceModel> criteriaBuilder = criteriaBuilderFactory.create(entityManager, PostPersistenceModel.class);

            // Subquery um CommentPersistenceModel mit übergebener commentId, inklusive replies (rekursiv) zu finden
            // String subqueryString = "SELECT c FROM Comment c WHERE c.commentId = :commentId OR c.parentComment IS NOT NULL OR c.replies IS NOT EMPTY";
            // https://persistence.blazebit.com/documentation/1.5/core/manual/en_US/
            CriteriaBuilder<CommentPersistenceModel> subquery = criteriaBuilderFactory.create(entityManager, CommentPersistenceModel.class);
            subquery.whereOr()
                    .where("id").eq(commentId)
                    .where("parentComment").isNotNull()
                    .where("replies").isNotEmpty()
                    .endOr();

            // Subquery mi PostPersistenceModel joinen im Feld "comments"
            // String queryString = "SELECT p FROM Post p WHERE :subquery MEMBER OF p.comments";
            criteriaBuilder.where("comments").in(subquery).end();   // Beendet Subquery und returnt zu Parent Query

            PostPersistenceModel post = criteriaBuilder.getSingleResult();
            return Result.success(PostPersistenceModel.Converter.toDomainEntity(post));
        } catch (NoResultException e) {
            log.error("getPostByCommentId: No Post Found", e);
            return Result.error("getPostByCommentId: No Post Found");
        } catch (PersistenceException e) {
            log.error("getPostByCommentId: Persistence Error", e);
            return Result.error("getPostByCommentId: Persistence Error");
        } catch (Exception e) {
            log.error("getPostByCommentId Error", e);
            return Result.error("getPostByCommentId Error");
        }

    }

    @Override
    public Result<Post> deletePost(UUID postId) {
        try {
            PostPersistenceModel post = entityManager.find(PostPersistenceModel.class, postId);
            if (post != null) {
                entityManager.remove(post);
                return Result.success(PostPersistenceModel.Converter.toDomainEntity(post));
            }
            return Result.notFound();
        } catch (EntityExistsException | IllegalArgumentException | TransactionRequiredException e) {
            log.error("Delete Error", e);
            return Result.error("Could not persist Post");
        }
    }


    @Override
    public Result<Post> savePost(Post post) {
        PostPersistenceModel postPersistenceModel = PostPersistenceModel.Converter.toPersistenceModel(post);
        try {
            entityManager.persist(postPersistenceModel);
            return Result.success(PostPersistenceModel.Converter.toDomainEntity(postPersistenceModel));
        } catch (EntityExistsException | IllegalArgumentException | TransactionRequiredException e) {
            log.error("savePost Error", e);
            return Result.error("Could not persist Post");
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
            log.error("UpdatePost Error", e);
            return Result.error("Could not persist Post");
        }
    }

    // HILFSMETHODEN
    private Result<List<Post>> getPostResultList(boolean includeComments, CriteriaBuilder<PostPersistenceModel> criteriaBuilder) {
        if (!includeComments) {
            List<PostPersistenceView> postList;
            CriteriaBuilder<PostPersistenceView> criteriaBuilderView = entityViewManager.applySetting(EntityViewSetting.create(PostPersistenceView.class), criteriaBuilder);
            postList = criteriaBuilderView.getResultList();
            return Result.success(postList.stream().map(PostPersistenceView::toDomainEntity).toList());
        }
        List<PostPersistenceModel> postList;
        postList = criteriaBuilder.getResultList();
        return Result.success(postList.stream().map(PostPersistenceModel.Converter::toDomainEntity).toList());
    }
}
