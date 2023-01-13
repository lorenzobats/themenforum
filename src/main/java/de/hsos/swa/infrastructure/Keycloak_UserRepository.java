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
    public void registerUser(String name, String password, String role, String userId) throws Exception {
        Keycloak_User user = new Keycloak_User(name, password, role, userId);
        try {
            entityManager.persist(user);
        } catch (EntityExistsException | IllegalArgumentException | TransactionRequiredException e) {
            throw new Exception("Keycloack User could not be registered");
        }
    }
}
