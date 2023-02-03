package de.hsos.swa.infrastructure.authorization;


import com.blazebit.persistence.CriteriaBuilder;
import com.blazebit.persistence.CriteriaBuilderFactory;
import de.hsos.swa.application.output.auth.AuthorizationGateway;
import de.hsos.swa.application.output.auth.dto.in.AuthorizationResult;
import de.hsos.swa.application.output.auth.dto.out.SaveAuthUserCommand;
import de.hsos.swa.infrastructure.authorization.model.AuthUser;
import org.jboss.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.*;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;
import java.util.Optional;
import java.util.UUID;

@RequestScoped
@Transactional(value = Transactional.TxType.MANDATORY)
public class AuthorizationAdapter implements AuthorizationGateway {

    @Inject
    EntityManager entityManager;

    @Inject
    CriteriaBuilderFactory criteriaBuilderFactory;

    @Inject
    Logger log;

    @Override
    public AuthorizationResult<Void> registerUser(SaveAuthUserCommand outputPortRequest) {
        CriteriaBuilder<AuthUser> query = criteriaBuilderFactory.create(entityManager, AuthUser.class);
        query.where("username").eq(outputPortRequest.getUsername());

        try{
            if(query.getResultList().isEmpty())
                // Für den Fall, dass der Nutzername belegt ist.
                return AuthorizationResult.notAuthenticated();
        } catch (Exception e) {
            return AuthorizationResult.exception();
        }

        AuthUser authUserEntity = new AuthUser(
                outputPortRequest.getUsername(),
                outputPortRequest.getPassword(),
                outputPortRequest.getRole(),
                outputPortRequest.getUserId());

        try {
            entityManager.persist(authUserEntity);
            return AuthorizationResult.ok();
        } catch (ConstraintViolationException | PersistenceException | IllegalArgumentException e) {
            return AuthorizationResult.exception();
        }
    }


    @Override
    public AuthorizationResult<String> getUserAuthRole(UUID userId) {
        Query query = entityManager.createNamedQuery("AuthUser.findRoleByUserId");
        query.setParameter("userId", userId);
        try {
            String role = (String) query.getSingleResult();
            return AuthorizationResult.ok(role);
        } catch (EntityExistsException | IllegalArgumentException | TransactionRequiredException e) {
            log.error("getUserAuthRole Error", e);
            return AuthorizationResult.exception();
        }
    }

    @Override
    public AuthorizationResult<Boolean> canAccessVotes(String username) {
        Optional<String> role = this.getUserRole(username);
        if(role.isPresent()){
            if(role.get().equals("admin")){
                return AuthorizationResult.ok();
            }
        }
        return AuthorizationResult.notAuthenticated();
    }

    @Override
    public AuthorizationResult<Boolean> canAccessUsers(String username) {
        Optional<String> role = this.getUserRole(username);
        if(role.isPresent()){
            if(role.get().equals("admin")){
                return AuthorizationResult.ok();
            }
        }
        return AuthorizationResult.notAuthenticated();
    }

    @Override
    public AuthorizationResult<Boolean> canAccessVotesBy(String username, String voteOwner) {
        // TODO:
        return AuthorizationResult.ok();
    }

    private Optional<String> getUserRole(String username) {
        Query query = entityManager.createNamedQuery("AuthUser.findRoleByUserName");
        query.setParameter("username", username);
        try {
            String role = (String) query.getSingleResult();
            return Optional.of(role);
        } catch (EntityExistsException | IllegalArgumentException | TransactionRequiredException e) {
            log.warn(e);
            return Optional.empty();
        }
    }


}
