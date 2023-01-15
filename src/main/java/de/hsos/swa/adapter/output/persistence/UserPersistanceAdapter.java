package de.hsos.swa.adapter.output.persistence;

import de.hsos.swa.application.port.output.checkUsernameAvailability.CheckUsernameAvailabilityOutputPort;
import de.hsos.swa.application.port.output.checkUsernameAvailability.CheckUsernameAvailabilityOutputPortRequest;
import de.hsos.swa.application.port.output.checkUsernameAvailability.CheckUsernameAvailabilityOutputPortResponse;
import de.hsos.swa.application.port.output.createUser.CreateUserOutputPortRequest;
import de.hsos.swa.application.port.output.createUser.CreateUserOutputPort;
import de.hsos.swa.application.port.output.createUser.CreateUserOutputPortResponse;
import de.hsos.swa.application.port.output.getUserByName.GetUserByNameOutputPort;
import de.hsos.swa.application.port.output.getUserByName.GetUserByNameOutputPortRequest;
import de.hsos.swa.application.port.output.getUserByName.GetUserByNameOutputPortResponse;
import org.jboss.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.TransactionRequiredException;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@RequestScoped
@Transactional(value = Transactional.TxType.MANDATORY)
public class UserPersistanceAdapter implements
        CreateUserOutputPort,
        GetUserByNameOutputPort,
        CheckUsernameAvailabilityOutputPort {

    @Inject
    EntityManager entityManager;

    @Inject
    Logger log;


    @Override
    public CreateUserOutputPortResponse createUser(CreateUserOutputPortRequest outputPortRequest) {
        UserPersistanceEntity userPersistanceEntity = new UserPersistanceEntity(
                outputPortRequest.getUsername()
        );
        try {
            entityManager.persist(userPersistanceEntity);
            return new CreateUserOutputPortResponse(userPersistanceEntity.id, userPersistanceEntity.name);
        } catch (EntityExistsException | IllegalArgumentException | TransactionRequiredException e) {
            log.error("Customer Entity could not be created", e);
        }
        return null;
    }

    @Override
    public GetUserByNameOutputPortResponse getUserByName(GetUserByNameOutputPortRequest outputPortRequest) {
        TypedQuery<UserPersistanceEntity> query = entityManager.createNamedQuery("UserPersistanceEntity.findByUsername", UserPersistanceEntity.class);
        query.setParameter("username", outputPortRequest.getUsername());
        UserPersistanceEntity userPersistanceEntity;
        try {
            userPersistanceEntity = query.getSingleResult();
        } catch (Exception e) {
            log.warn("GetUserByName Error", e);
            return null;
        }
        return new GetUserByNameOutputPortResponse(userPersistanceEntity.id, userPersistanceEntity.name);
    }

    @Override
    public CheckUsernameAvailabilityOutputPortResponse isUserNameAvailable(CheckUsernameAvailabilityOutputPortRequest outputPortRequest) {
        try {
            List<UserPersistanceEntity> userList = entityManager.createNamedQuery("UserPersistanceEntity.findByUsername", UserPersistanceEntity.class)
                    .setParameter("username", outputPortRequest.getUsername())
                    .getResultList();
            return new CheckUsernameAvailabilityOutputPortResponse(userList.isEmpty());
        } catch (Exception e) {
            log.warn("GetCustomerById Error", e);
            return null;
        }

    }
}
