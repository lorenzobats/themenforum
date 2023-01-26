package de.hsos.swa.infrastructure.auth;

import de.hsos.swa.application.output.auth.getUserAuthRole.GetUserAuthRoleOutputPort;
import de.hsos.swa.application.util.Result;
import de.hsos.swa.application.output.auth.createUserAuth.CreateUserAuthOutputPortRequest;
import de.hsos.swa.application.output.auth.createUserAuth.CreateUserAuthOutputPort;
import de.hsos.swa.application.output.auth.createUserAuth.CreateUserAuthOutputPortResponse;
import org.jboss.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.UUID;

@RequestScoped
@Transactional(value = Transactional.TxType.MANDATORY)
public class UserAuthAdapter implements
        CreateUserAuthOutputPort,
        GetUserAuthRoleOutputPort {

    @Inject
    EntityManager entityManager;

    @Inject
    Logger log;

    @Override
    public Result<CreateUserAuthOutputPortResponse> createUserAuth(CreateUserAuthOutputPortRequest outputPortRequest) {
        UserAuthEntity userAuthEntity = new UserAuthEntity(
                outputPortRequest.getUsername(),
                outputPortRequest.getPassword(),
                outputPortRequest.getRole(),
                outputPortRequest.getUserId());

        try {
            entityManager.persist(userAuthEntity);
            return Result.success(new CreateUserAuthOutputPortResponse(userAuthEntity.id, userAuthEntity.username));
        } catch (EntityExistsException | IllegalArgumentException | TransactionRequiredException e) {
            log.error("Customer Auth Entity could not be created", e);
            return Result.error("Customer Auth Entity could not be created");
        }
    }

    @Override
    public Result<String> getUserAuthRole(UUID userId) {
        Query query = entityManager.createNamedQuery("UserAuthEntity.findRoleByUserId");
        query.setParameter("userId", userId);
        try {
            String role = (String) query.getSingleResult();
            return Result.success(role);
        } catch (EntityExistsException | IllegalArgumentException | TransactionRequiredException e) {
            log.error("getUserAuthRole Error", e);
            return Result.error("getUserAuthRole Error");
        }
    }
}
