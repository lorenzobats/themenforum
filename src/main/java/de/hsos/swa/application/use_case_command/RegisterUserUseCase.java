package de.hsos.swa.application.use_case_command;

import de.hsos.swa.application.input.dto.in.RegisterUserInputPortRequest;
import de.hsos.swa.application.input.RegisterUserInputPort;
import de.hsos.swa.application.output.repository.RepositoryResult;
import de.hsos.swa.application.output.repository.UserRepository;
import de.hsos.swa.application.output.auth.createUserAuth.CreateUserAuthOutputPortRequest;
import de.hsos.swa.application.output.auth.createUserAuth.CreateUserAuthOutputPort;
import de.hsos.swa.application.output.auth.createUserAuth.CreateUserAuthOutputPortResponse;
import de.hsos.swa.application.util.Result;
import de.hsos.swa.domain.entity.User;
import de.hsos.swa.domain.factory.UserFactory;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;


@RequestScoped
@Transactional(Transactional.TxType.REQUIRES_NEW)
public class RegisterUserUseCase implements RegisterUserInputPort {

    @Inject
    UserRepository userRepository;
    @Inject
    CreateUserAuthOutputPort createUserAuthOutputPort;

    @Override
    public Result<User> registerUser(RegisterUserInputPortRequest request) {
        RepositoryResult<Boolean> existsUserWithNameResult = this.userRepository.existsUserWithName(request.username());

        if (existsUserWithNameResult.badResult()) {
            return Result.error("Registration failed");
        }

        if (existsUserWithNameResult.get()) {
            return Result.error("Username already taken");
        }

        User user = UserFactory.createUser(request.username());
        CreateUserAuthOutputPortRequest createUserAuthRequest = new CreateUserAuthOutputPortRequest(
                request.username(),
                request.password(),
                "member",
                user.getId());
        Result<CreateUserAuthOutputPortResponse> createUserAuthResponse = this.createUserAuthOutputPort.createUserAuth(createUserAuthRequest);


        RepositoryResult<User> createUserResponse = this.userRepository.saveUser(user);

        if (!createUserResponse.ok()) {
            return Result.error("Registration failed");
        }

        if (!createUserAuthResponse.isSuccessful()) {
            return Result.error("Authentication failed");
        }

        return Result.success(createUserResponse.get());

    }

}
