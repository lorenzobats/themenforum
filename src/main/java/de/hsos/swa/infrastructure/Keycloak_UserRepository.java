package de.hsos.swa.infrastructure;

import de.hsos.swa.control.dto.UserRegistrationDto;
import de.hsos.swa.entity.repository.UserAuthRepository;
import de.hsos.swa.infrastructure.dto.Keycloak_User;
import org.jboss.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.TransactionRequiredException;
import javax.transaction.Transactional;

@RequestScoped
@Transactional(value = Transactional.TxType.MANDATORY)
public class Keycloak_UserRepository implements UserAuthRepository {

    @Inject
    EntityManager entityManager;

    @Inject
    Logger log;

    @Override
    public boolean registerUser(String name, String password, String role, String userId) {
        Keycloak_User user = new Keycloak_User(name, password, role, userId);
        try {
            entityManager.persist(user);
            return true;
        } catch (EntityExistsException | IllegalArgumentException | TransactionRequiredException e) {
            log.warn("Keycloack User could not be inserted", e);
        }
        return false;
    }
}
