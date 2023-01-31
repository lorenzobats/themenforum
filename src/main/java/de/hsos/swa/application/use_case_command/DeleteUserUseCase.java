package de.hsos.swa.application.use_case_command;

import de.hsos.swa.application.input.DeleteUserInputPort;
import de.hsos.swa.application.input.dto.in.DeleteUserInputPortRequest;
import de.hsos.swa.application.output.auth.getUserAuthRole.GetUserAuthRoleOutputPort;
import de.hsos.swa.application.output.repository.RepositoryResult;
import de.hsos.swa.application.output.repository.TopicRepository;
import de.hsos.swa.application.output.repository.UserRepository;
import de.hsos.swa.application.util.Result;
import de.hsos.swa.domain.entity.User;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

@RequestScoped
@Transactional(Transactional.TxType.REQUIRES_NEW)
public class DeleteUserUseCase implements DeleteUserInputPort {
    @Inject
    UserRepository userRepository;

    @Inject
    GetUserAuthRoleOutputPort userAuthRoleOutputPort;

    /**
     * Deaktiviert einen User auf Basis der übergebenen Informationen.
     * @param request enthält Themen-ID und Nutzernamen der Lösch-Anfrage
     * @return Result<Topic> enthält gelöschtes Thema bzw. Fehlermeldung bei Misserfolg
     */
    @Override
    public Result<User> deleteUser(DeleteUserInputPortRequest request) {
        RepositoryResult<User> requestingUserResult = this.userRepository.getUserByName(request.username());
        if (requestingUserResult.badResult()) {
            return Result.error("Cannot find user " + request.username());
        }
        User requestingUser = requestingUserResult.get();

        Result<String> roleResult = this.userAuthRoleOutputPort.getUserAuthRole(requestingUser.getId());
        if (!roleResult.isSuccessful()) {
            return Result.error("Cannot find user role " + request.username());
        }
        String role = roleResult.getData();

        if(!role.equals("admin")){
            return Result.error("Not allowed to disable user");
        }

        RepositoryResult<User> userResult = this.userRepository.getUserById(request.userId());
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
