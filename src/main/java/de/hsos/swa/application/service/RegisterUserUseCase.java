package de.hsos.swa.application.service;

import de.hsos.swa.application.port.input._shared.Result;
import de.hsos.swa.application.port.input.registerUser.RegisterUserInputPortRequest;
import de.hsos.swa.application.port.input.registerUser.RegisterUserInputPortResponse;
import de.hsos.swa.application.port.input.registerUser.RegisterUserInputPort;
import de.hsos.swa.application.port.output.checkUsernameAvailability.CheckUsernameAvailabilityOutputPort;
import de.hsos.swa.application.port.output.checkUsernameAvailability.CheckUsernameAvailabilityOutputPortRequest;
import de.hsos.swa.application.port.output.checkUsernameAvailability.CheckUsernameAvailabilityOutputPortResponse;
import de.hsos.swa.application.port.output.createUser.CreateUserOutputPortRequest;
import de.hsos.swa.application.port.output.createUser.CreateUserOutputPort;
import de.hsos.swa.application.port.output.createUser.CreateUserOutputPortResponse;
import de.hsos.swa.application.port.output.createUserAuth.CreateUserAuthOutputPortRequest;
import de.hsos.swa.application.port.output.createUserAuth.CreateUserAuthOutputPort;
import de.hsos.swa.application.port.output.createUserAuth.CreateUserAuthOutputPortResponse;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.UUID;

// TODO: Domain Service: Nutzernamen müssen bestimmten Schema entsprechen (als Idee um mehr "Business Logik") zu implementieren.

@ApplicationScoped
public class RegisterUserUseCase implements RegisterUserInputPort {

    @Inject
    CreateUserOutputPort createUserOutputPort;
    @Inject
    CreateUserAuthOutputPort createUserAuthOutputPort;
    @Inject
    CheckUsernameAvailabilityOutputPort checkUsernameAvailabilityOutputPort;

    @Override
    public Result<RegisterUserInputPortResponse> registerUser(RegisterUserInputPortRequest inputPortRequest) {
        // 1. Überprüfen, ob der Nutzername bereits existiert
        CheckUsernameAvailabilityOutputPortRequest checkUsernameAvailabilityRequest = new CheckUsernameAvailabilityOutputPortRequest(inputPortRequest.getUsername());
        Result<CheckUsernameAvailabilityOutputPortResponse> checkUsernameAvailabilityResponse = this.checkUsernameAvailabilityOutputPort.isUserNameAvailable(checkUsernameAvailabilityRequest);

        if(!checkUsernameAvailabilityResponse.isSuccessful()) {
            return Result.error("Registration failed");
        }

        if (!checkUsernameAvailabilityResponse.getData().isUserNameAvailable()) {
            return Result.error("Username already taken");
        }

        // 2. User Entity erzeugen
        CreateUserOutputPortRequest createUserRequest = new CreateUserOutputPortRequest(inputPortRequest.getUsername());
        Result<CreateUserOutputPortResponse> createUserResponse = this.createUserOutputPort.createUser(createUserRequest);

        if(!createUserResponse.isSuccessful()) {
            return Result.error("Registration failed");
        }

        // 3. User Auth erzeugen
        CreateUserAuthOutputPortRequest createUserAuthRequest = new CreateUserAuthOutputPortRequest(
                inputPortRequest.getUsername(),
                inputPortRequest.getPassword(),
                "member",
                createUserResponse.getData().getId());

        Result<CreateUserAuthOutputPortResponse> createUserAuthResponse = this.createUserAuthOutputPort.createUserAuth(createUserAuthRequest);

        if(!createUserAuthResponse.isSuccessful()) {
            return Result.error("Registration failed");
        }

        return Result.success(
                new RegisterUserInputPortResponse(
                        createUserAuthResponse.getData().getId(),
                        createUserAuthResponse.getData().getUsername()));

    }

}
