package de.hsos.swa.adapter.output.auth;

import de.hsos.swa.application.port.input._shared.Result;
import de.hsos.swa.application.port.output.user.createUserAuth.CreateUserAuthOutputPortRequest;
import de.hsos.swa.application.port.output.user.createUserAuth.CreateUserAuthOutputPort;
import de.hsos.swa.application.port.output.user.createUserAuth.CreateUserAuthOutputPortResponse;
import org.jboss.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.TransactionRequiredException;
import javax.transaction.Transactional;

@RequestScoped
@Transactional(value = Transactional.TxType.MANDATORY)
public class UserAuthAdapter implements CreateUserAuthOutputPort {

    @Inject
    EntityManager entityManager;

    @Inject
    Logger log;

    @Override
    public Result<CreateUserAuthOutputPortResponse> createUserAuth(CreateUserAuthOutputPortRequest outputPortRequest) {
        UserAuthEntity userAuthEntity = new UserAuthEntity(
                outputPortRequest.getUsername(),
                outputPortRequest.getPassword(),
                outputPortRequest.getRole(),
                outputPortRequest.getUserId());

        try {
            entityManager.persist(userAuthEntity);
            return Result.success(new CreateUserAuthOutputPortResponse(userAuthEntity.id, userAuthEntity.username));
        } catch (EntityExistsException | IllegalArgumentException | TransactionRequiredException e) {
            log.error("Customer Auth Entity could not be created", e);
            return Result.exception(e);
        }
    }
}
