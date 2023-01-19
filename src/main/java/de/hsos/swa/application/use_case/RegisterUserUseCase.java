package de.hsos.swa.application.use_case;

import de.hsos.swa.application.port.input._shared.Result;
import de.hsos.swa.application.port.input.registerUser.RegisterUserInputPortRequest;
import de.hsos.swa.application.port.input.registerUser.RegisterUserInputPortResponse;
import de.hsos.swa.application.port.input.registerUser.RegisterUserInputPort;
import de.hsos.swa.application.port.output.UserRepository;
import de.hsos.swa.application.port.output.auth.createUserAuth.CreateUserAuthOutputPortRequest;
import de.hsos.swa.application.port.output.auth.createUserAuth.CreateUserAuthOutputPort;
import de.hsos.swa.application.port.output.auth.createUserAuth.CreateUserAuthOutputPortResponse;
import de.hsos.swa.domain.entity.User;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.UUID;

// TODO: Domain Service: Nutzernamen müssen bestimmten Schema entsprechen (als Idee um mehr "Business Logik") zu implementieren.

@ApplicationScoped
public class RegisterUserUseCase implements RegisterUserInputPort {

    @Inject
    UserRepository userRepository;
    @Inject
    CreateUserAuthOutputPort createUserAuthOutputPort;

    @Override
    public Result<RegisterUserInputPortResponse> registerUser(RegisterUserInputPortRequest inputPortRequest) {
        // 1. Überprüfen, ob der Nutzername bereits existiert
        Result<Boolean> checkUsernameAvailabilityResponse = this.userRepository.isUserNameAvailable(inputPortRequest.getUsername());

        if(!checkUsernameAvailabilityResponse.isSuccessful()) {
            return Result.error("Registration failed");
        }

        if (!checkUsernameAvailabilityResponse.getData()) {
            return Result.error("Username already taken");
        }

        // 2.A > DOMAIN  > User Entity erzeugen --> TODO: In domain Service auslagern, oder Factory?
        User user = new User(inputPortRequest.getUsername());


        // 3. User Auth erzeugen
        CreateUserAuthOutputPortRequest createUserAuthRequest = new CreateUserAuthOutputPortRequest(
                inputPortRequest.getUsername(),
                inputPortRequest.getPassword(),
                "member",
                user.getId());
        Result<CreateUserAuthOutputPortResponse> createUserAuthResponse = this.createUserAuthOutputPort.createUserAuth(createUserAuthRequest);


        // 2.B > PERSISTENCE > User Persistieren
        Result<UUID> createUserResponse = this.userRepository.saveUser(user);

        if(!createUserResponse.isSuccessful()) {
            // TODO: UserAuth wieder löschen falls was schief gegangen ist bei Persistierung.
            return Result.error("Registration failed");
        }

        if(!createUserAuthResponse.isSuccessful()) {
            return Result.error("Registration failed");
        }

        return Result.success(
                new RegisterUserInputPortResponse(
                        createUserAuthResponse.getData().getId(),
                        createUserAuthResponse.getData().getUsername()));

    }

}
