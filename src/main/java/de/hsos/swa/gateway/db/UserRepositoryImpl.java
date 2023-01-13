package de.hsos.swa.gateway.db;

import de.hsos.swa.entity.User;
import de.hsos.swa.entity.repository.UserRepository;
import de.hsos.swa.gateway.db.dto.User_JpaEntity;
import org.jboss.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.TransactionRequiredException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequestScoped
@Transactional(value = Transactional.TxType.MANDATORY)
public class UserRepositoryImpl implements UserRepository {

    @Inject
    EntityManager entityManager;

    @Inject
    Logger log;

    @Override
    public Optional<User> addUser(User user) {
        User_JpaEntity userJpaEntity = User_JpaEntity.Converter.toJpaEntity(user);
        try {
            entityManager.persist(userJpaEntity);
            User userFromDb = User_JpaEntity.Converter.toEntity(userJpaEntity);
            return Optional.of(userFromDb);
        } catch (EntityExistsException | IllegalArgumentException | TransactionRequiredException e) {
            log.warn("Customer Entity could not be created", e);
        }
        return Optional.empty();
    }


    @Override
    public Optional<User> getUserById(UUID userId) {
        return Optional.empty();
    }

    @Override
    public boolean usernameExists(String username) {
        try {
            List<User_JpaEntity> userList = entityManager.createNamedQuery("User_JpaEntity.findByUsername", User_JpaEntity.class)
                    .setParameter("username", username).getResultList();
            return !userList.isEmpty();
        } catch (Exception e) {
            log.warn("GetCustomerById Error", e);
            return false;
        }
    }
}
