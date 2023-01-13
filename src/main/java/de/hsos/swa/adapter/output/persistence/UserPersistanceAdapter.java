package de.hsos.swa.adapter.output.persistence;

import de.hsos.swa.application.port.output.createUser.CreateUserCommand;
import de.hsos.swa.application.port.output.createUser.CreateUserOutputPort;
import de.hsos.swa.application.port.output.createUser.CreateUserResult;
import org.jboss.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.TransactionRequiredException;
import javax.transaction.Transactional;

@RequestScoped
@Transactional(value = Transactional.TxType.MANDATORY)
public class UserPersistanceAdapter implements CreateUserOutputPort {

    @Inject
    EntityManager entityManager;

    @Inject
    Logger log;


    @Override
    public CreateUserResult createUser(CreateUserCommand command) {
        UserPersistanceEntity userPersistanceEntity = new UserPersistanceEntity(
                command.getUsername()
        );
        try {
            entityManager.persist(userPersistanceEntity);

            return new CreateUserResult(userPersistanceEntity.id, userPersistanceEntity.name);
        } catch (EntityExistsException | IllegalArgumentException | TransactionRequiredException e) {
            log.error("Customer Entity could not be created", e);
        }
        return null;
    }
}
