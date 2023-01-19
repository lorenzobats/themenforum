package de.hsos.swa.adapter.output.persistence.user;

import de.hsos.swa.application.port.input._shared.Result;
import de.hsos.swa.application.port.output.UserRepository;
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
public class UserPersistenceAdapter implements UserRepository {

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
    public Result<User> getUserByName(String username) {
        TypedQuery<UserPersistenceEntity> query = entityManager.createNamedQuery("UserPersistenceEntity.findByUsername", UserPersistenceEntity.class);
        query.setParameter("username", username);
        UserPersistenceEntity userPersistenceEntity;
        try {
            userPersistenceEntity = query.getSingleResult();
            return Result.success(UserPersistenceEntity.Converter.toDomainEntity(userPersistenceEntity));
        } catch (Exception e) {
            log.error("GetUserByName Error", e);
            return Result.exception(e);
        }
    }

    @Override
    public Result<User> getUserById(String userId) {
        TypedQuery<UserPersistenceEntity> query = entityManager.createNamedQuery("UserPersistenceEntity.findById", UserPersistenceEntity.class);
        query.setParameter("id", userId);
        UserPersistenceEntity user;
        try {
            user = query.getSingleResult();
            return Result.success(UserPersistenceEntity.Converter.toDomainEntity(user));
        } catch (Exception e) {
            log.error("GetUserById Error", e);
            return Result.exception(e);
        }
    }

    @Override
    public Result<Boolean> isUserNameAvailable(String username) {
        try {
            List<UserPersistenceEntity> userList = entityManager.createNamedQuery("UserPersistenceEntity.findByUsername", UserPersistenceEntity.class)
                    .setParameter("username", username)
                    .getResultList();
            return Result.success(userList.isEmpty());
        } catch (Exception e) {
            log.error("GetCustomerById Error", e);
            return Result.exception(e);
        }
    }
}
