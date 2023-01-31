package de.hsos.swa.infrastructure.persistence;

import com.blazebit.persistence.CriteriaBuilder;
import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.view.EntityViewManager;
import de.hsos.swa.application.output.repository.RepositoryResult;
import de.hsos.swa.application.output.repository.UserRepository;
import de.hsos.swa.domain.entity.User;
import de.hsos.swa.infrastructure.persistence.model.UserPersistenceModel;
import org.jboss.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@RequestScoped
@Transactional(value = Transactional.TxType.MANDATORY)
public class UserPersistenceAdapter implements UserRepository {

    @Inject
    EntityManager entityManager;

    @Inject
    CriteriaBuilderFactory criteriaBuilderFactory;

    @Inject
    Logger log;

    // CREATE
    @Override
    public RepositoryResult<User> saveUser(User user) {
        UserPersistenceModel userPersistenceModel = UserPersistenceModel.Converter.toPersistenceModel(user);
        try {
            entityManager.persist(userPersistenceModel);
            return RepositoryResult.ok(UserPersistenceModel.Converter.toDomainEntity(userPersistenceModel));
        } catch (EntityExistsException e) {
            return RepositoryResult.notPersisted();
        } catch (IllegalArgumentException | TransactionRequiredException e) {
            log.error(e);
            return RepositoryResult.error();
        }
    }

    // READ
    @Override
    public RepositoryResult<List<User>> getAllUsers() {
        try {
            CriteriaBuilder<UserPersistenceModel> criteriaBuilder = criteriaBuilderFactory.create(entityManager, UserPersistenceModel.class);
            List<UserPersistenceModel> postList;
            postList = criteriaBuilder.getResultList();
            return RepositoryResult.ok(postList.stream().map(UserPersistenceModel.Converter::toDomainEntity).toList());
        } catch (NoResultException e) {
            return RepositoryResult.notFound();
        } catch (PersistenceException e) {
            log.error(e);
            return RepositoryResult.error();
        }
    }

    @Override
    public RepositoryResult<User> getUserByName(String username) {
        TypedQuery<UserPersistenceModel> query = entityManager
                .createNamedQuery("UserPersistenceModel.findByUsername", UserPersistenceModel.class)
                .setParameter("username", username);
        return getUserRepositoryResult(query);
    }

    @Override
    public RepositoryResult<User> getUserById(UUID userId) {
        TypedQuery<UserPersistenceModel> query = entityManager
                .createNamedQuery("UserPersistenceModel.findById", UserPersistenceModel.class)
                .setParameter("id", userId);
        return getUserRepositoryResult(query);
    }

    @Override
    public RepositoryResult<Boolean> existsUserWithName(String username) {
        try {
            UserPersistenceModel user = entityManager.find(UserPersistenceModel.class, username);
            return RepositoryResult.ok(user == null);
        } catch (IllegalArgumentException e) {
            log.error(e);
            return RepositoryResult.error();
        }
    }

    // UPDATE
    @Override
    public RepositoryResult<User> updateUser(User user) {
        UserPersistenceModel userPersistenceModel = UserPersistenceModel.Converter.toPersistenceModel(user);
        try {
            entityManager.merge(userPersistenceModel);
            return RepositoryResult.ok(UserPersistenceModel.Converter.toDomainEntity(userPersistenceModel));
        } catch (NoResultException e) {
            return RepositoryResult.notFound();
        } catch (IllegalArgumentException | TransactionRequiredException e) {
            log.error(e);
            return RepositoryResult.error();
        }
    }


    // HILFSMETHODEN
    private RepositoryResult<User> getUserRepositoryResult(TypedQuery<UserPersistenceModel> query) {
        try {
            UserPersistenceModel user = query.getSingleResult();
            return RepositoryResult.ok(UserPersistenceModel.Converter.toDomainEntity(user));
        } catch (NoResultException e) {
            return RepositoryResult.notFound();
        } catch (IllegalArgumentException e) {
            log.error(e);
            return RepositoryResult.error();
        }
    }
}
