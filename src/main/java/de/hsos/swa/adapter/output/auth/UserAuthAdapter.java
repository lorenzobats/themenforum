package de.hsos.swa.adapter.output.auth;

import de.hsos.swa.application.port.output.createUserAuth.CreateUserAuthCommand;
import de.hsos.swa.application.port.output.createUserAuth.CreateUserAuthOutputPort;
import de.hsos.swa.application.port.output.createUserAuth.CreateUserAuthResult;
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
    public CreateUserAuthResult createUserAuth(CreateUserAuthCommand command) {
        UserAuthEntity userAuthEntity = new UserAuthEntity(
                command.getUsername(),
                command.getPassword(),
                command.getRole(),
                command.getUserId()
        );
        try {
            entityManager.persist(userAuthEntity);
            return new CreateUserAuthResult(userAuthEntity.id, userAuthEntity.username);
        } catch (EntityExistsException | IllegalArgumentException | TransactionRequiredException e) {
            log.error("Customer Auth Entity could not be created", e);
        }
        return null;
    }
}
