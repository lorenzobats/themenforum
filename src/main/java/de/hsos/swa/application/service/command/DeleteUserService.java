package de.hsos.swa.application.service.command;

import de.hsos.swa.application.annotations.ApplicationService;
import de.hsos.swa.application.input.DeleteUserUseCase;
import de.hsos.swa.application.input.dto.in.DeleteUserCommand;
import de.hsos.swa.application.input.dto.out.ApplicationResult;
import de.hsos.swa.application.output.auth.AuthorizationGateway;
import de.hsos.swa.application.output.repository.UserRepository;
import de.hsos.swa.domain.entity.User;
import de.hsos.swa.application.output.auth.dto.in.AuthorizationResult;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.UUID;

@RequestScoped
@Transactional(Transactional.TxType.REQUIRES_NEW)
@ApplicationService
public class DeleteUserService implements DeleteUserUseCase {
    @Inject
    UserRepository userRepository;

    @Inject
    AuthorizationGateway authorizationGateway;

    /**
     * Deaktiviert einen User auf Basis der übergebenen Informationen.
     * @param command enthält Themen-ID und Nutzernamen der Lösch-Anfrage
     * @return ApplicationResult<Topic> enthält gelöschtes Thema bzw. Fehlermeldung bei Misserfolg
     */
    @Override
    public ApplicationResult<User> deleteUser(DeleteUserCommand command) {
        de.hsos.swa.application.output.repository.dto.out.RepositoryResult<User> requestingUserResult = this.userRepository.getUserByName(command.username());
        if (requestingUserResult.error()) {
            return ApplicationResult.exception("Cannot find user " + command.username());
        }
        User requestingUser = requestingUserResult.get();

        AuthorizationResult<String> roleResult = this.authorizationGateway.getUserAuthRole(requestingUser.getId());
        if (roleResult.denied()) {
            return ApplicationResult.exception("Cannot find user role " + command.username());
        }
        String role = roleResult.get();

        if(!role.equals("admin")){
            return ApplicationResult.exception("Not allowed to disable user");
        }

        de.hsos.swa.application.output.repository.dto.out.RepositoryResult<User> userResult = this.userRepository.getUserById(UUID.fromString(command.userId()));
        if (userResult.error()) {
            return ApplicationResult.exception("Cannot find userId " + command.userId());
        }
        User user = userResult.get();

        // TODO Methode Delete
        user.setName("DELETED");
        // Todo. Auth Set Role "disabled"

        de.hsos.swa.application.output.repository.dto.out.RepositoryResult<User> updateUserResult = this.userRepository.updateUser(user);
        if (updateUserResult.error()) {
            return ApplicationResult.exception("Cannot update post ");
        }
        return ApplicationResult.ok(user);
    }
}
