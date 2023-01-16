package de.hsos.swa.adapter.output.persistence.post;

import de.hsos.swa.adapter.output.persistence.user.UserPersistenceEntity;
import de.hsos.swa.application.port.input._shared.Result;
import de.hsos.swa.application.port.output.post.getPostById.GetPostByIdOutputPort;
import de.hsos.swa.application.port.output.post.getPostById.GetPostByIdOutputPortRequest;
import de.hsos.swa.application.port.output.post.getPostById.GetPostByIdOutputPortResponse;
import de.hsos.swa.application.port.output.post.savePost.SavePostOutputPort;
import de.hsos.swa.application.port.output.post.savePost.SavePostOutputPortRequest;
import de.hsos.swa.application.port.output.post.savePost.SavePostOutputPortResponse;
import de.hsos.swa.application.port.output.user.getUserByName.GetUserByNameOutputPortResponse;
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
        GetPostByIdOutputPort {

    @Inject
    EntityManager entityManager;

    @Inject
    Logger log;

    @Override
    public Result<SavePostOutputPortResponse> savePost(SavePostOutputPortRequest request) {
        PostPersistenceEntity post = PostPersistenceEntity.Converter.toPersistenceEntity(request.getPost());
        try {
            entityManager.persist(post);
            return Result.success(new SavePostOutputPortResponse(post.id));
        } catch (EntityExistsException | IllegalArgumentException | TransactionRequiredException e) {
            log.error("User Entity could not be created", e);
            return Result.exception(e);
        }
    }

    @Override
    public Result<GetPostByIdOutputPortResponse> getPostById(GetPostByIdOutputPortRequest request) {
        TypedQuery<PostPersistenceEntity> query = entityManager.createNamedQuery("PostPersistenceEntity.findById", PostPersistenceEntity.class);
        query.setParameter("id", UUID.fromString(request.getId()));
        PostPersistenceEntity post;
        try {
            post = query.getSingleResult();
            return Result.success(new GetPostByIdOutputPortResponse(PostPersistenceEntity.Converter.toDomainEntity(post)));
        } catch (Exception e) {
            log.error("GetPostByName Error", e);
            return Result.exception(e);
        }
    }
}
