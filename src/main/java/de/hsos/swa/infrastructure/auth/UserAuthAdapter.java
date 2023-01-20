package de.hsos.swa.infrastructure.auth;

import de.hsos.swa.application.input.Result;
import de.hsos.swa.application.output.auth.createUserAuth.CreateUserAuthOutputPortRequest;
import de.hsos.swa.application.output.auth.createUserAuth.CreateUserAuthOutputPort;
import de.hsos.swa.application.output.auth.createUserAuth.CreateUserAuthOutputPortResponse;
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
