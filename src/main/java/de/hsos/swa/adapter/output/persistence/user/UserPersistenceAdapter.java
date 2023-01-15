package de.hsos.swa.adapter.output.persistence.user;

import de.hsos.swa.application.port.input._shared.Result;
import de.hsos.swa.application.port.output.checkUsernameAvailability.CheckUsernameAvailabilityOutputPort;
import de.hsos.swa.application.port.output.checkUsernameAvailability.CheckUsernameAvailabilityOutputPortRequest;
import de.hsos.swa.application.port.output.checkUsernameAvailability.CheckUsernameAvailabilityOutputPortResponse;
import de.hsos.swa.application.port.output.createUser.CreateUserOutputPort;
import de.hsos.swa.application.port.output.createUser.CreateUserOutputPortRequest;
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
public class UserPersistenceAdapter implements
        CreateUserOutputPort,
        GetUserByNameOutputPort,
        CheckUsernameAvailabilityOutputPort {

    @Inject
    EntityManager entityManager;

    @Inject
    Logger log;

    @Override
    public Result<CreateUserOutputPortResponse> createUser(CreateUserOutputPortRequest outputPortRequest) {
        UserPersistenceEntity userPersistenceEntity = new UserPersistenceEntity(outputPortRequest.getUsername());
        try {
            entityManager.persist(userPersistenceEntity);
            return Result.success(new CreateUserOutputPortResponse(userPersistenceEntity.id, userPersistenceEntity.name));
        } catch (EntityExistsException | IllegalArgumentException | TransactionRequiredException e) {
            log.error("Customer Entity could not be created", e);
            return Result.exception(e);
        }
    }

    @Override
    public Result<GetUserByNameOutputPortResponse> getUserByName(GetUserByNameOutputPortRequest outputPortRequest) {
        TypedQuery<UserPersistenceEntity> query = entityManager.createNamedQuery("UserPersistenceEntity.findByUsername", UserPersistenceEntity.class);
        query.setParameter("username", outputPortRequest.getUsername());
        UserPersistenceEntity userPersistenceEntity;
        try {
            userPersistenceEntity = query.getSingleResult();
            return Result.success(new GetUserByNameOutputPortResponse(userPersistenceEntity.id, userPersistenceEntity.name));
        } catch (Exception e) {
            log.error("GetUserByName Error", e);
            return Result.exception(e);
        }
    }

    @Override
    public Result<CheckUsernameAvailabilityOutputPortResponse> isUserNameAvailable(CheckUsernameAvailabilityOutputPortRequest outputPortRequest) {
        try {
            List<UserPersistenceEntity> userList = entityManager.createNamedQuery("UserPersistenceEntity.findByUsername", UserPersistenceEntity.class)
                    .setParameter("username", outputPortRequest.getUsername())
                    .getResultList();
            return Result.success(new CheckUsernameAvailabilityOutputPortResponse(userList.isEmpty()));
        } catch (Exception e) {
            log.error("GetCustomerById Error", e);
            return Result.exception(e);
        }

    }
}
