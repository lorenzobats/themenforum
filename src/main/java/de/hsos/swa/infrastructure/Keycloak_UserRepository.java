package de.hsos.swa.infrastructure;

import org.jboss.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.TransactionRequiredException;
import javax.transaction.Transactional;

@RequestScoped
@Transactional(value = Transactional.TxType.MANDATORY)
public class Keycloak_UserRepository {

    @Inject
    EntityManager entityManager;

    @Inject
    Logger log;

    public void addKeycloakUser(String username, String password, String role, String userId) {
        Keycloak_User user = new Keycloak_User(username, password, role, userId);
        try {
            entityManager.persist(user);
        } catch (EntityExistsException | IllegalArgumentException | TransactionRequiredException e) {
            log.warn("Keycloack User could not be inserted", e);
        }
    }

}
