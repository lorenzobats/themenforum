package de.hsos.swa.infrastructure.persistence;

import de.hsos.swa.application.output.Result;
import de.hsos.swa.application.output.repository.UserRepository;
import de.hsos.swa.domain.entity.User;
import de.hsos.swa.infrastructure.persistence.model.UserPersistenceModel;
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
public class UserPersistenceAdapter implements UserRepository {

    @Inject
    EntityManager entityManager;

    @Inject
    Logger log;

    @Override
    public Result<User> saveUser(User user) {
        UserPersistenceModel userPersistenceModel = UserPersistenceModel.Converter.toPersistenceModel(user);
        try {
            entityManager.persist(userPersistenceModel);
            return Result.success(UserPersistenceModel.Converter.toDomainEntity(userPersistenceModel));
        } catch (EntityExistsException | IllegalArgumentException | TransactionRequiredException e) {
            log.error("Customer Entity could not be created", e);
            return Result.exception(e);
        }
    }


    @Override
    public Result<User> getUserByName(String username) {
        TypedQuery<UserPersistenceModel> query = entityManager.createNamedQuery("UserPersistenceModel.findByUsername", UserPersistenceModel.class);
        query.setParameter("username", username);
        UserPersistenceModel userPersistenceModel;
        try {
            userPersistenceModel = query.getSingleResult();
            return Result.success(UserPersistenceModel.Converter.toDomainEntity(userPersistenceModel));
        } catch (Exception e) {
            log.error("GetUserByName Error", e);
            return Result.exception(e);
        }
    }

    @Override
    public Result<User> getUserById(String userId) {
        TypedQuery<UserPersistenceModel> query = entityManager.createNamedQuery("UserPersistenceModel.findById", UserPersistenceModel.class);
        query.setParameter("id", userId);
        UserPersistenceModel user;
        try {
            user = query.getSingleResult();
            return Result.success(UserPersistenceModel.Converter.toDomainEntity(user));
        } catch (Exception e) {
            log.error("GetUserById Error", e);
            return Result.exception(e);
        }
    }

    @Override
    public Result<Boolean> isUserNameAvailable(String username) {
        try {
            List<UserPersistenceModel> userList = entityManager.createNamedQuery("UserPersistenceModel.findByUsername", UserPersistenceModel.class)
                    .setParameter("username", username)
                    .getResultList();
            return Result.success(userList.isEmpty());
        } catch (Exception e) {
            log.error("GetCustomerById Error", e);
            return Result.exception(e);
        }
    }
}
