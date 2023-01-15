package de.hsos.swa.adapter.output.persistence.post;

import de.hsos.swa.application.port.input._shared.Result;
import de.hsos.swa.application.port.output.savePost.SavePostOutputPort;
import de.hsos.swa.application.port.output.savePost.SavePostOutputPortRequest;
import de.hsos.swa.application.port.output.savePost.SavePostOutputPortResponse;
import org.jboss.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.TransactionRequiredException;
import javax.transaction.Transactional;

@RequestScoped
@Transactional(value = Transactional.TxType.MANDATORY)
public class PostPersistenceAdapter implements
        SavePostOutputPort {

    @Inject
    EntityManager entityManager;

    @Inject
    Logger log;

    @Override
    public Result<SavePostOutputPortResponse> savePost(SavePostOutputPortRequest inputPortRequest) {
        PostPersistenceEntity postPersistenceEntity = PostPersistenceEntity.Converter.toPersistenceEntity(inputPortRequest.getPost());
        try {
            entityManager.persist(postPersistenceEntity);
            return Result.success(new SavePostOutputPortResponse(postPersistenceEntity.id));
        } catch (EntityExistsException | IllegalArgumentException | TransactionRequiredException e) {
            log.error("User Entity could not be created", e);
            return Result.exception(e);
        }
    }
}
