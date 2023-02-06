package de.hsos.swa.infrastructure.authorization;

import com.blazebit.persistence.CriteriaBuilder;
import com.blazebit.persistence.CriteriaBuilderFactory;
import de.hsos.swa.application.output.auth.AuthorizationGateway;
import de.hsos.swa.application.output.auth.dto.in.AuthorizationResult;
import de.hsos.swa.application.output.auth.dto.out.RegisterUserCommand;
import de.hsos.swa.infrastructure.authorization.model.AuthUser;
import de.hsos.swa.infrastructure.authorization.model.OwnerOf;
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

    //------------------------------------------------------------------------------------------------------------------
    // READ PERMISSION
    @Override
    public AuthorizationResult<Void> registerUser(RegisterUserCommand command) {
        CriteriaBuilder<AuthUser> query = criteriaBuilderFactory.create(entityManager, AuthUser.class);
        query.where("username").eq(command.getUsername());

        try{
            if(!query.getResultList().isEmpty())
                // Für den Fall, dass der Nutzername belegt ist.
                return AuthorizationResult.notAuthenticated();
        } catch (Exception e) {
            return AuthorizationResult.exception();
        }

        AuthUser authUserEntity = new AuthUser(
                command.getUsername(),
                command.getPassword(),
                command.getRole(),
                command.getUserId());

        try {
            entityManager.persist(authUserEntity);
            return AuthorizationResult.ok();
        } catch (ConstraintViolationException | PersistenceException | IllegalArgumentException e) {
            return AuthorizationResult.exception();
        }
    }
    @Override
    public AuthorizationResult<Void> disableUser(String username) {
        Optional<AuthUser> optionalAuthUser = this.loadUser(username);
        if(optionalAuthUser.isEmpty())
            return AuthorizationResult.exception();

        // Deaktivierte Nutzer
        AuthUser owner = optionalAuthUser.get();
        owner.disable();

        try{
            entityManager.merge(owner);
            return AuthorizationResult.ok();
        } catch (PersistenceException e) {
            return AuthorizationResult.exception();
        }
    }

    //------------------------------------------------------------------------------------------------------------------
    // READ PERMISSION
    public AuthorizationResult<Void> addOwnership(String owningUser, UUID ressourceId){
        Optional<AuthUser> optionalAuthUser = this.loadUser(owningUser);
        if(optionalAuthUser.isEmpty())
            return AuthorizationResult.notAuthenticated();

        AuthUser owner = optionalAuthUser.get();
        if(!owner.isActive())
            return AuthorizationResult.notAllowed();

        try{
            OwnerOf ownerOf = new OwnerOf(owner, ressourceId);
            entityManager.merge(ownerOf);
            return AuthorizationResult.ok();
        } catch (PersistenceException e) {
            return AuthorizationResult.exception();
        }
    }
    @Override
    public AuthorizationResult<Void> removeOwnership(String owningUser, UUID ressourceId){
        Optional<OwnerOf> optionalOwnerOf = this.loadOwnership(ressourceId);
        if(optionalOwnerOf.isEmpty())
            return AuthorizationResult.exception();

        OwnerOf ownerOf = optionalOwnerOf.get();
        try{
            entityManager.remove(ownerOf);
            return AuthorizationResult.ok();
        } catch (PersistenceException e) {
            log.warn(e);
            return AuthorizationResult.exception();
        }
    }


    //------------------------------------------------------------------------------------------------------------------
    // READ ACCESS
    @Override
    public AuthorizationResult<Boolean> canAccessVotes(String accessingUser) {
        // Admins dürfen alle Votes einsehen
        if(isActiveAdmin(accessingUser))
            return AuthorizationResult.ok();

        return AuthorizationResult.notAuthenticated();
    }

    @Override
    public AuthorizationResult<Boolean> canAccessUsers(String accessingUser) {
        // Admins dürfen alle User einsehen
        if(isActiveAdmin(accessingUser))
            return AuthorizationResult.ok();

        return AuthorizationResult.notAuthenticated();
    }

    @Override
    public AuthorizationResult<Boolean> canAccessVotesBy(String accessingUser, String votesOwner) {
        // Admins dürfen alle Votes abrufen
        if(isActiveAdmin(accessingUser))
            return AuthorizationResult.ok();

        // Member dürfen ihre eigenen Votes abrufen
        if(accessingUser.equals(votesOwner))
            return AuthorizationResult.ok();

        return AuthorizationResult.notAuthenticated();
    }

    //------------------------------------------------------------------------------------------------------------------
    // DELETE PERMISSION
    @Override
    public AuthorizationResult<Boolean> canDeleteUser(String accessingUser, String username) {
        // Nur Admins dürfen User deaktivieren/löschen
        if(isActiveAdmin(accessingUser))
            // Admins können sich nicht selbst deaktivieren
            if(!accessingUser.equals(username)){
                return AuthorizationResult.ok();
            }
        return AuthorizationResult.notAllowed();
    }
    @Override
    public AuthorizationResult<Boolean> canDeleteTopic(String accessingUser, UUID TopicId) {
        // Admins dürfen alle Votes löschen
        if(isActiveAdmin(accessingUser))
            return AuthorizationResult.ok();
        // Member dürfen keine Topics löschen
        return AuthorizationResult.notAllowed();
    }
    @Override
    public AuthorizationResult<Boolean> canDeletePost(String accessingUser, UUID postId) {
        // Admins dürfen alle Posts löschen
        if(isActiveAdmin(accessingUser))
            return AuthorizationResult.ok();
        // Member dürfen ihre eigenen Posts löschen
        if(isActiveRessourceOwner(accessingUser, postId))
            return AuthorizationResult.ok();

        return AuthorizationResult.notAllowed();
    }
    @Override
    public AuthorizationResult<Boolean> canDeleteComment(String accessingUser, UUID commentId) {
        // Admins dürfen alle Kommentare löschen
        if(isActiveAdmin(accessingUser))
            return AuthorizationResult.ok();
        // Member dürfen ihre eigenen Kommentare löschen
        if(isActiveRessourceOwner(accessingUser, commentId))
            return AuthorizationResult.ok();

        return AuthorizationResult.notAllowed();
    }
    @Override
    public AuthorizationResult<Boolean> canDeleteVote(String accessingUser, UUID voteId) {
        // Admins dürfen alle Votes löschen
        if(isActiveAdmin(accessingUser))
            return AuthorizationResult.ok();
        // Member dürfen ihre eigenen Votes löschen
        if(isActiveRessourceOwner(accessingUser, voteId))
            return AuthorizationResult.ok();
        return AuthorizationResult.notAllowed();
    }


    //------------------------------------------------------------------------------------------------------------------
    // UPDATE PERMISSION
    @Override
    public AuthorizationResult<Boolean> canUpdatePost(String accessingUser, UUID commentId) {
        // Admins dürfen alle Posts updaten
        if(isActiveAdmin(accessingUser))
            return AuthorizationResult.ok();
        // Member dürfen ihre eigenen Posts updaten
        if(isActiveRessourceOwner(accessingUser, commentId))
            return AuthorizationResult.ok();

        return AuthorizationResult.notAllowed();
    }

    //------------------------------------------------------------------------------------------------------------------
    // UTILITY METHODS
    private Optional<AuthUser> loadUser(String username) {
        CriteriaBuilder<AuthUser> query = criteriaBuilderFactory.create(entityManager, AuthUser.class);
        query.where("username").eq(username);
        try {
            AuthUser user = query.getSingleResult();
            return Optional.of(user);
        } catch (EntityExistsException | IllegalArgumentException | TransactionRequiredException e) {
            log.warn(e);
            return Optional.empty();
        }
    }

    private Optional<OwnerOf> loadOwnership(UUID ressourceId) {
        CriteriaBuilder<OwnerOf> query = criteriaBuilderFactory.create(entityManager, OwnerOf.class);
        query.where("ressourceId").eq(ressourceId);

        try {
            OwnerOf ownerOf = query.getSingleResult();
            return Optional.of(ownerOf);
        } catch (IllegalArgumentException | PersistenceException e) {
            log.warn(e);
            return Optional.empty();
        }
    }

    private boolean isActiveAdmin(String username) {
        Optional<AuthUser> optionalAuthUser = this.loadUser(username);
        if(optionalAuthUser.isPresent()) {
            AuthUser user = optionalAuthUser.get();
            return user.isActive() && user.getRole().equals("admin");
        }
        return false;
    }

    private boolean isActiveRessourceOwner(String accessingUser, UUID ressourceId) {
        try {
            CriteriaBuilder<OwnerOf> query = criteriaBuilderFactory.create(entityManager, OwnerOf.class);
            query
                    .where("owner.username").eq(accessingUser)
                    .where("owner.active").eq(true)
                    .where("ressourceId").eq(ressourceId);
            return !query.getResultList().isEmpty();
        } catch (EntityExistsException | IllegalArgumentException | TransactionRequiredException e) {
            return false;
        }
    }
}
