package de.hsos.swa.infrastructure.persistence;

import com.blazebit.persistence.CriteriaBuilder;
import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.view.EntityViewManager;
import com.blazebit.persistence.view.EntityViewSetting;
import de.hsos.swa.application.PostFilterParams;
import de.hsos.swa.application.util.Result;
import de.hsos.swa.application.output.repository.PostRepository;
import de.hsos.swa.domain.entity.Post;
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

            if (!includeComments) {
                List<PostPersistenceView> postList;
                CriteriaBuilder<PostPersistenceView> criteriaBuilderView = entityViewManager.applySetting(EntityViewSetting.create(PostPersistenceView.class), criteriaBuilder);
                postList = criteriaBuilderView.getResultList();
                return Result.success(postList.stream().map(PostPersistenceView::toDomainEntity).toList());
            }
            List<PostPersistenceModel> postList;
            postList = criteriaBuilder.getResultList();
            return Result.success(postList.stream().map(PostPersistenceModel.Converter::toDomainEntity).toList());
        } catch (Exception e) {
            log.error("getAllFilteredPosts", e);
            return Result.exception(e);
        }
    }

    @Override
    public Result<List<Post>> getAllFilteredPosts(Map<PostFilterParams, Object> filterParams, boolean includeComments) {

        try {
            CriteriaBuilder<PostPersistenceModel> criteriaBuilder = criteriaBuilderFactory.create(entityManager, PostPersistenceModel.class);
            if (filterParams.containsKey(PostFilterParams.USERNAME))
                criteriaBuilder.where("userPersistenceModel.name").eq(filterParams.get(PostFilterParams.USERNAME));
            if (filterParams.containsKey(PostFilterParams.USERID))
                criteriaBuilder.where("userPersistenceModel.id").eq(filterParams.get(PostFilterParams.USERID));
            if (filterParams.containsKey(PostFilterParams.DATE_FROM))
                criteriaBuilder.where("createdAt").ge(filterParams.get(PostFilterParams.DATE_FROM));
            if (filterParams.containsKey(PostFilterParams.DATE_TO))
                criteriaBuilder.where("createdAt").le(filterParams.get(PostFilterParams.DATE_TO));
            // TODO: Sort By und Order By

            if (!includeComments) {
                List<PostPersistenceView> postList;
                CriteriaBuilder<PostPersistenceView> criteriaBuilderView = entityViewManager.applySetting(EntityViewSetting.create(PostPersistenceView.class), criteriaBuilder);
                postList = criteriaBuilderView.getResultList();
                return Result.success(postList.stream().map(PostPersistenceView::toDomainEntity).toList());
            }
            List<PostPersistenceModel> postList;
            postList = criteriaBuilder.getResultList();
            return Result.success(postList.stream().map(PostPersistenceModel.Converter::toDomainEntity).toList());
        } catch (Exception e) {
            log.error("getAllFilteredPosts", e);
            return Result.exception(e);
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
        } catch (EntityExistsException | IllegalArgumentException | TransactionRequiredException e) {
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
