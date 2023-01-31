package de.hsos.swa.application.service.command;

import de.hsos.swa.application.annotations.ApplicationService;
import de.hsos.swa.application.input.dto.in.RegisterUserCommand;
import de.hsos.swa.application.input.RegisterUserUseCase;
import de.hsos.swa.application.output.auth.AuthorizationGateway;
import de.hsos.swa.application.output.repository.RepositoryResult;
import de.hsos.swa.application.output.repository.UserRepository;
import de.hsos.swa.application.output.auth.dto.out.SaveAuthUserCommand;
import de.hsos.swa.application.input.dto.out.Result;
import de.hsos.swa.domain.entity.User;
import de.hsos.swa.domain.factory.UserFactory;
import de.hsos.swa.application.output.auth.dto.in.AuthorizationResult;
import org.jboss.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;


@RequestScoped
@Transactional(Transactional.TxType.REQUIRES_NEW)
@ApplicationService
public class RegisterUserService implements RegisterUserUseCase {

    @Inject
    UserRepository userRepository;
    @Inject
    AuthorizationGateway authorizationGateway;

    @Inject
    Logger log;

    @Override
    // TODO:
    public Result<User> registerUser(RegisterUserCommand request) {
        log.debug(">>>" + "startRegisterUser");
        RepositoryResult<Boolean> existsUserWithNameResult = this.userRepository.existsUserWithName(request.username());

        if (existsUserWithNameResult.badResult()) {
            return Result.error("Registration failed");
        }

        if (existsUserWithNameResult.get()) {
            return Result.error("Username already taken");
        }

        User user = UserFactory.createUser(request.username());
        SaveAuthUserCommand createUserAuthRequest = new SaveAuthUserCommand(
                request.username(),
                request.password(),
                "member",
                user.getId());
        log.debug(">>>" + user.getId());
        AuthorizationResult<Void> createUserAuthResponse = this.authorizationGateway.createUserAuth(createUserAuthRequest);


        RepositoryResult<User> createUserResponse = this.userRepository.saveUser(user);

        if (!createUserResponse.ok()) {
            return Result.error("Registration failed");
        }

        if (createUserAuthResponse.invalid()) {
            return Result.error("Authentication failed");
        }

        return Result.success(createUserResponse.get());

    }

}
