package de.hsos.swa.infrastructure.persistence;

import com.blazebit.persistence.CriteriaBuilder;
import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.view.EntityViewManager;
import com.blazebit.persistence.view.EntityViewSetting;
import de.hsos.swa.application.output.repository.dto.out.RepositoryResult;
import de.hsos.swa.application.service.query.params.PostFilterParams;
import de.hsos.swa.application.output.repository.PostRepository;
import de.hsos.swa.domain.entity.Post;
import de.hsos.swa.infrastructure.persistence.cte.CommentCTE;
import de.hsos.swa.infrastructure.persistence.model.CommentPersistenceModel;
import de.hsos.swa.infrastructure.persistence.model.PostPersistenceModel;
import de.hsos.swa.infrastructure.persistence.view.PostPersistenceView;
import org.jboss.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 *
 */
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


    //------------------------------------------------------------------------------------------------------------------
    // COMMANDS
    @Override
    public RepositoryResult<Post> savePost(Post post) {
        PostPersistenceModel postPersistenceModel = PostPersistenceModel.Converter.toPersistenceModel(post);
        try {
            entityManager.persist(postPersistenceModel);
            return RepositoryResult.ok(PostPersistenceModel.Converter.toDomainEntity(postPersistenceModel));
        } catch (IllegalArgumentException e) {
            log.warn(e);
            return RepositoryResult.exception();
        } catch (PersistenceException e) {
            log.error(e);
            return RepositoryResult.exception();
        }
    }

    @Override
    @Transactional(value = Transactional.TxType.MANDATORY)
    public RepositoryResult<Post> updatePost(Post post) {
        PostPersistenceModel postPersistenceModel = PostPersistenceModel.Converter.toPersistenceModel(post);
        try {
            entityManager.merge(postPersistenceModel);
            return RepositoryResult.ok(PostPersistenceModel.Converter.toDomainEntity(postPersistenceModel));
        } catch (IllegalArgumentException e) {
            log.warn(e);
            return RepositoryResult.exception();
        } catch (PersistenceException e) {
            log.error(e);
            return RepositoryResult.exception();
        }
    }

    @Override
    public RepositoryResult<Post> deletePost(UUID postId) {
        try {
            PostPersistenceModel post = entityManager.find(PostPersistenceModel.class, postId);
            if (post != null) {
                entityManager.remove(post);
                return RepositoryResult.ok(PostPersistenceModel.Converter.toDomainEntity(post));
            }
            return RepositoryResult.notFound();
        } catch (NoResultException e) {
            return RepositoryResult.notFound();
        } catch (IllegalArgumentException e) {
            log.warn(e);
            return RepositoryResult.exception();
        } catch (PersistenceException e) {
            log.error(e);
            return RepositoryResult.exception();
        }
    }

    //------------------------------------------------------------------------------------------------------------------
    // QUERIES
    @Override
    public RepositoryResult<List<Post>> getAllPosts(boolean includeComments) {
        try {
            CriteriaBuilder<PostPersistenceModel> criteriaBuilder = criteriaBuilderFactory.create(entityManager, PostPersistenceModel.class);
            return getPostResultList(includeComments, criteriaBuilder);
        } catch (NoResultException e) {
            return RepositoryResult.notFound();
        } catch (IllegalArgumentException e) {
            log.warn(e);
            return RepositoryResult.exception();
        } catch (PersistenceException e) {
            log.error(e);
            return RepositoryResult.exception();
        }
    }

    @Override
    public RepositoryResult<List<Post>> getFilteredPosts(Map<PostFilterParams, Object> filterParams, boolean includeComments) {
        try {
            CriteriaBuilder<PostPersistenceModel> criteriaBuilder = criteriaBuilderFactory.create(entityManager, PostPersistenceModel.class);

            // Optionales Anwenden der FilterParameter
            if (filterParams.containsKey(PostFilterParams.USERNAME))
                criteriaBuilder.where("userPersistenceModel.name").eq(filterParams.get(PostFilterParams.USERNAME))
                        .where("userPersistenceModel.active").eq(true);

            if (filterParams.containsKey(PostFilterParams.USERID))
                criteriaBuilder.where("userPersistenceModel.entityId").eq(filterParams.get(PostFilterParams.USERID));

            if (filterParams.containsKey(PostFilterParams.TOPIC))
                criteriaBuilder.where("topicPersistenceModel.title").eq(filterParams.get(PostFilterParams.TOPIC));

            if (filterParams.containsKey(PostFilterParams.TOPICID))
                criteriaBuilder.where("topicPersistenceModel.id").eq(filterParams.get(PostFilterParams.TOPICID));

            if (filterParams.containsKey(PostFilterParams.DATE_FROM))
                criteriaBuilder.where("createdAt").ge(filterParams.get(PostFilterParams.DATE_FROM));

            if (filterParams.containsKey(PostFilterParams.DATE_TO))
                criteriaBuilder.where("createdAt").le(filterParams.get(PostFilterParams.DATE_TO));

            return getPostResultList(includeComments, criteriaBuilder);
        } catch (NoResultException e) {
            return RepositoryResult.notFound();
        } catch (IllegalArgumentException e) {
            log.warn(e);
            return RepositoryResult.exception();
        } catch (PersistenceException e) {
            log.error(e);
            return RepositoryResult.exception();
        }
    }

    @Override
    public RepositoryResult<Post> getPostById(UUID postId, boolean includeComments) {
        try {
            CriteriaBuilder<PostPersistenceModel> criteriaBuilder = criteriaBuilderFactory.create(entityManager, PostPersistenceModel.class);
            criteriaBuilder.where("id").eq(postId);
            if (!includeComments) {
                CriteriaBuilder<PostPersistenceView> criteriaBuilderView = entityViewManager.applySetting(EntityViewSetting.create(PostPersistenceView.class), criteriaBuilder);
                PostPersistenceView post = criteriaBuilderView.getSingleResult();
                return RepositoryResult.ok(PostPersistenceView.toDomainEntity(post));
            } else {
                PostPersistenceModel post = criteriaBuilder.getSingleResult();
                return RepositoryResult.ok(PostPersistenceModel.Converter.toDomainEntity(post));
            }
        } catch (NoResultException e) {
            return RepositoryResult.notFound();
        } catch (IllegalArgumentException e) {
            log.warn(e);
            return RepositoryResult.exception();
        } catch (PersistenceException e) {
            log.error(e);
            return RepositoryResult.exception();
        }
    }

    @Override
    public RepositoryResult<Post> getPostByCommentId(UUID commentId) {
        try {
            // Rekursive CTE zur Suche des zur übergebenen commentID zugehörigen Kommentars auf erster Ebene
            CriteriaBuilder<UUID> subquery = criteriaBuilderFactory.create(entityManager, UUID.class)
                    .withRecursive(CommentCTE.class)
                    .from(CommentPersistenceModel.class, "comment")
                    .bind("id").select("comment.id")
                    .bind("parentComment").select("comment.parentComment")
                    .where("id").eq(commentId)
                    .unionAll()
                    .from(CommentPersistenceModel.class, "comment")
                    .from(CommentCTE.class, "previous_comment")
                    .bind("id").select("comment.id")
                    .bind("parentComment").select("comment.parentComment")
                    .where("comment.id").eqExpression("previous_comment.parentComment.id")
                    .end()
                    .where("parentComment").isNull()
                    .from(CommentCTE.class, "first_level_comment")
                    .select("first_level_comment.id");

            // Nutzen der zuvor formulierten Subquery, um Post zu finden
            CriteriaBuilder<PostPersistenceModel> criteriaBuilder = criteriaBuilderFactory.create(entityManager, PostPersistenceModel.class);
            criteriaBuilder.where("comments").in(subquery).end();

            PostPersistenceModel post = criteriaBuilder.getSingleResult();
            return RepositoryResult.ok(PostPersistenceModel.Converter.toDomainEntity(post));
        } catch (NoResultException e) {
            return RepositoryResult.notFound();
        } catch (IllegalArgumentException e) {
            log.warn(e);
            return RepositoryResult.exception();
        } catch (PersistenceException e) {
            log.error(e);
            return RepositoryResult.exception();
        }
    }

    //------------------------------------------------------------------------------------------------------------------
    // UTILITY METHODS
    private RepositoryResult<List<Post>> getPostResultList(boolean includeComments, CriteriaBuilder<PostPersistenceModel> criteriaBuilder) {
        try {
            if (!includeComments) {
                List<PostPersistenceView> postList;
                CriteriaBuilder<PostPersistenceView> criteriaBuilderView = entityViewManager.applySetting(EntityViewSetting.create(PostPersistenceView.class), criteriaBuilder);
                postList = criteriaBuilderView.getResultList();
                return RepositoryResult.ok(postList.stream().map(PostPersistenceView::toDomainEntity).toList());
            }
            List<PostPersistenceModel> postList;
            postList = criteriaBuilder.getResultList();
            return RepositoryResult.ok(postList.stream().map(PostPersistenceModel.Converter::toDomainEntity).toList());
        } catch (NoResultException e) {
            return RepositoryResult.notFound();
        } catch (IllegalArgumentException e) {
            log.warn(e);
            return RepositoryResult.exception();
        } catch (PersistenceException e) {
            log.error(e);
            return RepositoryResult.exception();
        }
    }
}
