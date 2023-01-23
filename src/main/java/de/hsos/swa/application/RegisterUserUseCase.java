package de.hsos.swa.application;

import de.hsos.swa.application.input.dto.in.RegisterUserInputPortRequest;
import de.hsos.swa.application.input.RegisterUserInputPort;
import de.hsos.swa.application.output.repository.UserRepository;
import de.hsos.swa.application.output.auth.createUserAuth.CreateUserAuthOutputPortRequest;
import de.hsos.swa.application.output.auth.createUserAuth.CreateUserAuthOutputPort;
import de.hsos.swa.application.output.auth.createUserAuth.CreateUserAuthOutputPortResponse;
import de.hsos.swa.application.util.Result;
import de.hsos.swa.domain.entity.User;
import de.hsos.swa.domain.factory.UserFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;


@ApplicationScoped
public class RegisterUserUseCase implements RegisterUserInputPort {

    @Inject
    UserRepository userRepository;
    @Inject
    CreateUserAuthOutputPort createUserAuthOutputPort;

    @Override
    public Result<User> registerUser(RegisterUserInputPortRequest request) {
        Result<Boolean> checkUsernameAvailabilityResponse = this.userRepository.isUserNameAvailable(request.username());

        if (!checkUsernameAvailabilityResponse.isSuccessful()) {
            return Result.error("Registration failed");
        }

        if (!checkUsernameAvailabilityResponse.getData()) {
            return Result.error("Username already taken");
        }

        User user = UserFactory.createUser(request.username());

        CreateUserAuthOutputPortRequest createUserAuthRequest = new CreateUserAuthOutputPortRequest(
                request.username(),
                request.password(),
                "member",
                user.getId());
        Result<CreateUserAuthOutputPortResponse> createUserAuthResponse = this.createUserAuthOutputPort.createUserAuth(createUserAuthRequest);


        // 2.B > PERSISTENCE > User Persistieren
        Result<User> createUserResponse = this.userRepository.saveUser(user);

        if (!createUserResponse.isSuccessful()) {
            // TODO: UserAuth wieder löschen falls was schief gegangen ist bei Persistierung.
            return Result.error("Registration failed");
        }

        if (!createUserAuthResponse.isSuccessful()) {
            return Result.error("Authentication failed");
        }

        return Result.success(createUserResponse.getData());

    }

}
