package de.hsos.swa.adapter.output.persistence.user;

import de.hsos.swa.application.port.input._shared.Result;
import de.hsos.swa.application.port.output.user.checkUsernameAvailability.CheckUsernameAvailabilityOutputPort;
import de.hsos.swa.application.port.output.user.checkUsernameAvailability.CheckUsernameAvailabilityOutputPortRequest;
import de.hsos.swa.application.port.output.user.checkUsernameAvailability.CheckUsernameAvailabilityOutputPortResponse;
import de.hsos.swa.application.port.output.user.getUserById.GetUserByIdOutputPort;
import de.hsos.swa.application.port.output.user.getUserById.GetUserByIdOutputPortRequest;
import de.hsos.swa.application.port.output.user.getUserById.GetUserByIdOutputPortResponse;
import de.hsos.swa.application.port.output.user.saveUser.SaveUserOutputPort;
import de.hsos.swa.application.port.output.user.getUserByName.GetUserByNameOutputPort;
import de.hsos.swa.application.port.output.user.getUserByName.GetUserByNameOutputPortRequest;
import de.hsos.swa.application.port.output.user.getUserByName.GetUserByNameOutputPortResponse;
import de.hsos.swa.domain.entity.User;
import org.jboss.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.TransactionRequiredException;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@RequestScoped
@Transactional(value = Transactional.TxType.MANDATORY)
public class UserPersistenceAdapter implements
        SaveUserOutputPort,
        GetUserByNameOutputPort,
        GetUserByIdOutputPort,
        CheckUsernameAvailabilityOutputPort {

    @Inject
    EntityManager entityManager;

    @Inject
    Logger log;


    @Override
    public Result<UUID> saveUser(User user) {
        UserPersistenceEntity userPersistenceEntity = UserPersistenceEntity.Converter.toPersistenceEntity(user);
        try {
            entityManager.persist(userPersistenceEntity);
            return Result.success(userPersistenceEntity.id);
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
    public Result<GetUserByIdOutputPortResponse> getUserById(GetUserByIdOutputPortRequest outputPortRequest) {
        TypedQuery<UserPersistenceEntity> query = entityManager.createNamedQuery("UserPersistenceEntity.findById", UserPersistenceEntity.class);
        query.setParameter("id", outputPortRequest.getUserId());
        UserPersistenceEntity userPersistenceEntity;
        try {
            userPersistenceEntity = query.getSingleResult();
            return Result.success(new GetUserByIdOutputPortResponse(userPersistenceEntity.id, userPersistenceEntity.name));
        } catch (Exception e) {
            log.error("GetUserById Error", e);
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
