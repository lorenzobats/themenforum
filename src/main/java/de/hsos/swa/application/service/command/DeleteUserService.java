package de.hsos.swa.application.service.command;

import de.hsos.swa.application.annotations.ApplicationService;
import de.hsos.swa.application.input.DeleteUserUseCase;
import de.hsos.swa.application.input.dto.in.DeleteUserCommand;
import de.hsos.swa.application.output.auth.AuthorizationGateway;
import de.hsos.swa.application.output.repository.dto.out.RepositoryResult;
import de.hsos.swa.application.output.repository.UserRepository;
import de.hsos.swa.application.input.dto.out.Result;
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
     * @param request enthält Themen-ID und Nutzernamen der Lösch-Anfrage
     * @return Result<Topic> enthält gelöschtes Thema bzw. Fehlermeldung bei Misserfolg
     */
    @Override
    public Result<User> deleteUser(DeleteUserCommand request) {
        RepositoryResult<User> requestingUserResult = this.userRepository.getUserByName(request.username());
        if (requestingUserResult.badResult()) {
            return Result.error("Cannot find user " + request.username());
        }
        User requestingUser = requestingUserResult.get();

        AuthorizationResult<String> roleResult = this.authorizationGateway.getUserAuthRole(requestingUser.getId());
        if (roleResult.invalid()) {
            return Result.error("Cannot find user role " + request.username());
        }
        String role = roleResult.get();

        if(!role.equals("admin")){
            return Result.error("Not allowed to disable user");
        }

        RepositoryResult<User> userResult = this.userRepository.getUserById(UUID.fromString(request.userId()));
        if (userResult.badResult()) {
            return Result.error("Cannot find userId " + request.userId());
        }
        User user = userResult.get();

        // TODO Methode Delete
        user.setName("DELETED");
        // Todo. Auth Set Role "disabled"

        RepositoryResult<User> updateUserResult = this.userRepository.updateUser(user);
        if (updateUserResult.badResult()) {
            return Result.error("Cannot update post ");
        }
        return Result.success(user);
    }
}
