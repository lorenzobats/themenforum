package de.hsos.swa.application.service.command;

import de.hsos.swa.application.annotations.ApplicationService;
import de.hsos.swa.application.input.command.DisableUserUseCase;
import de.hsos.swa.application.input.dto.in.DisableUserCommand;
import de.hsos.swa.application.input.dto.out.ApplicationResult;
import de.hsos.swa.application.output.auth.AuthorizationGateway;
import de.hsos.swa.application.output.repository.UserRepository;
import de.hsos.swa.application.output.repository.dto.in.RepositoryResult;
import de.hsos.swa.application.service.util.AuthorizationResultMapper;
import de.hsos.swa.domain.entity.User;
import de.hsos.swa.application.output.auth.dto.in.AuthorizationResult;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

/**
 * Die Application Service Klasse DisableUserService implementiert das Interface
 * DisableUserUseCase der Boundary des Application Hexagons.
 * Es realisiert die Applikationslogik für das Deaktivieren eines Users durch einen Admin
 *
 * @author Lorenzo Battiston
 * @author Oliver Schlüter
 * @version 1.0
 * @see DisableUserUseCase              Korrespondierender Input-Port für diesen Service
 * @see DisableUserCommand              Korrespondierendes Request DTO für diesen Service
 * @see UserRepository                  Output-Port zum Finden und Updaten des Users nach Deaktivierung
 * @see AuthorizationGateway            Output-Port zum Löschen der Zugangsdaten
 */
@RequestScoped
@Transactional(Transactional.TxType.REQUIRES_NEW)
@ApplicationService
public class DisableUserService implements DisableUserUseCase {
    @Inject
    UserRepository userRepository;

    @Inject
    AuthorizationGateway authorizationGateway;

    /**
     * Deaktiviert einen User auf Basis der übergebenen Informationen.
     *
     * @param command enthält ID des zu löschenden/deaktivierenden Users
     * @return ApplicationResult<Optional<User>> enthält deaktivierten User bzw. Fehlermeldung bei Misserfolg
     */
    @Override
    public ApplicationResult<Optional<User>> deleteUser(DisableUserCommand command, String requestingUser) {


        RepositoryResult<User> userResult = this.userRepository.getUserById(UUID.fromString(command.userId()));
        if (userResult.error())
            return ApplicationResult.noContent(Optional.empty());
        User user = userResult.get();
        if(!user.isActive())
            return ApplicationResult.noContent(Optional.empty());

        AuthorizationResult<Boolean> permission = authorizationGateway.canDeleteUser(requestingUser, user.getName());
        if(permission.denied())
            return AuthorizationResultMapper.handleRejection(permission.status());

        authorizationGateway.deleteUser(user.getName());
        user.disable();

        RepositoryResult<User> updateUserResult = this.userRepository.updateUser(user);
        if (updateUserResult.error())
            return ApplicationResult.exception("Cannot update user");
        return ApplicationResult.ok(Optional.of(user));
    }
}
