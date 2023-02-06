package de.hsos.swa.infrastructure.persistence;

import com.blazebit.persistence.CriteriaBuilder;
import com.blazebit.persistence.CriteriaBuilderFactory;
import de.hsos.swa.application.output.repository.dto.in.RepositoryResult;
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



    //------------------------------------------------------------------------------------------------------------------
    // COMMANDS
    @Override
    public RepositoryResult<User> saveUser(User user) {
        UserPersistenceModel userPersistenceModel = UserPersistenceModel.Converter.toPersistenceModel(user);
        try {
            entityManager.persist(userPersistenceModel);
            return RepositoryResult.ok(UserPersistenceModel.Converter.toDomainEntity(userPersistenceModel));
        } catch (EntityExistsException e) {
            return RepositoryResult.exception();
        } catch (IllegalArgumentException | TransactionRequiredException e) {
            log.error(e);
            return RepositoryResult.exception();
        }
    }

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
            return RepositoryResult.exception();
        }
    }

    //------------------------------------------------------------------------------------------------------------------
    // QUERIES
    @Override
    public RepositoryResult<List<User>> getAllUsers() {
        try {
            CriteriaBuilder<UserPersistenceModel> criteriaBuilder = criteriaBuilderFactory.create(entityManager, UserPersistenceModel.class);
            List<UserPersistenceModel> postList;
            postList = criteriaBuilder.getResultList();
            return RepositoryResult.ok(postList.stream().map(UserPersistenceModel.Converter::toDomainEntity).toList());
        } catch (NoResultException e) {
            return RepositoryResult.notFound();
        } catch (IllegalArgumentException e) {
            log.warn(e);
            return RepositoryResult.exception();
        } catch (PersistenceException e) {
            log.error(e);
            return RepositoryResult.exception();
        }
    }

    @Override
    public RepositoryResult<User> getUserByName(String username) {
        CriteriaBuilder<UserPersistenceModel> criteriaBuilder = criteriaBuilderFactory.create(entityManager, UserPersistenceModel.class);
        criteriaBuilder.where("name").eq(username);
        return getUserRepositoryResult(criteriaBuilder);
    }

    @Override
    public RepositoryResult<User> getUserById(UUID userId) {
        CriteriaBuilder<UserPersistenceModel> criteriaBuilder = criteriaBuilderFactory.create(entityManager, UserPersistenceModel.class);
        criteriaBuilder.where("id").eq(userId);
        return getUserRepositoryResult(criteriaBuilder);
    }

    // HILFSMETHODEN
    private RepositoryResult<User> getUserRepositoryResult(CriteriaBuilder<UserPersistenceModel> criteriaBuilder) {
        try {
            UserPersistenceModel user = criteriaBuilder.getSingleResult();
            return RepositoryResult.ok(UserPersistenceModel.Converter.toDomainEntity(user));
        } catch (NoResultException e) {
            return RepositoryResult.notFound();
        } catch (IllegalArgumentException e) {
            log.warn(e);
            return RepositoryResult.exception();
        } catch (PersistenceException e) {
            log.error(e);
            return RepositoryResult.exception();
        }
    }

}
