package de.hsos.swa.infrastructure.authorization;

import de.hsos.swa.application.output.auth.AuthorizationGateway;
import de.hsos.swa.application.output.auth.dto.in.AuthorizationResult;
import de.hsos.swa.application.output.auth.dto.out.SaveAuthUserCommand;
import org.jboss.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.UUID;

@RequestScoped
@Transactional(value = Transactional.TxType.MANDATORY)
public class AuthorizationAdapter implements
        AuthorizationGateway {

    @Inject
    EntityManager entityManager;

    @Inject
    Logger log;

    @Override
    public AuthorizationResult<Void> createUserAuth(SaveAuthUserCommand outputPortRequest) {
        UserAuthEntity authUserEntity = new UserAuthEntity(
                outputPortRequest.getUsername(),
                outputPortRequest.getPassword(),
                outputPortRequest.getRole(),
                outputPortRequest.getUserId());

        try {
            entityManager.persist(authUserEntity);
            return AuthorizationResult.ok();
        } catch (EntityExistsException | IllegalArgumentException | TransactionRequiredException e) {
            log.error("Customer Auth Entity could not be created", e);
            return AuthorizationResult.error();
        }
    }

    public AuthorizationResult<String> isAuthenticated(String username){
        return AuthorizationResult.error();
    }

    @Override
    public AuthorizationResult<String> getUserAuthRole(UUID userId) {
        Query query = entityManager.createNamedQuery("UserAuthEntity.findRoleByUserId");
        query.setParameter("userId", userId);
        try {
            String role = (String) query.getSingleResult();
            return AuthorizationResult.ok(role);
        } catch (EntityExistsException | IllegalArgumentException | TransactionRequiredException e) {
            log.error("getUserAuthRole Error", e);
            return AuthorizationResult.error();
        }
    }
}
