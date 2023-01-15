package de.hsos.swa.application.service;

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
    @Transactional // Frage was bringt das?
    public RegisterUserInputPortResponse registerUser(RegisterUserInputPortRequest inputPortRequest) {
        // 1. Überprüfen, ob der Nutzername bereits existiert
        CheckUsernameAvailabilityOutputPortRequest checkUsernameAvailabilityOutputPortRequest = new CheckUsernameAvailabilityOutputPortRequest(inputPortRequest.getUsername());
        CheckUsernameAvailabilityOutputPortResponse checkUsernameAvailabilityOutputPortResponse = this.checkUsernameAvailabilityOutputPort.isUserNameAvailable(checkUsernameAvailabilityOutputPortRequest);
        if (!checkUsernameAvailabilityOutputPortResponse.isUserNameAvailable()) {
            // TODO: Mit Optionals arbeiten, oder Exception zurückgeben, damit dem Nutzer entsprechende Fehlermessage zurückgegeben werden kann.
            // TODO: EVTL. Generell ne generische Response erschaffen in denen Errors weitergegeben werden?
            return new RegisterUserInputPortResponse(UUID.randomUUID(), "ALREADY TAKEN");
        }


        // 2. User Entity erzeugen
        CreateUserOutputPortRequest createUserOutputPortRequest = new CreateUserOutputPortRequest(inputPortRequest.getUsername());
        CreateUserOutputPortResponse createUserOutputPortResponse = this.createUserOutputPort.createUser(createUserOutputPortRequest);
        if(createUserOutputPortResponse == null) {
            // TODO: Mit Optionals arbeiten, oder Exception zurückgeben, dass bei Registration was schiefgelaufen ist? (Hier vllt. eher Optional)
            return null;
        }


        // 3. User Auth erzeugen
        CreateUserAuthOutputPortRequest createUserAuthOutputPortRequest = new CreateUserAuthOutputPortRequest(inputPortRequest.getUsername(), inputPortRequest.getPassword(), "member", createUserOutputPortResponse.getId());
        CreateUserAuthOutputPortResponse createUserAuthOutputPortResponse = this.createUserAuthOutputPort.createUserAuth(createUserAuthOutputPortRequest);
        if(createUserAuthOutputPortResponse == null) {
            // TODO: Mit Optionals arbeiten, oder Exception zurückgeben, dass bei Registration was schiefgelaufen ist? (Hier vllt. eher Optional)
            // TODO: Hier ebenfalls rollback falls erster teil geglückt ist, aber zweiter Teil nicht.
            return null;
        }

        RegisterUserInputPortResponse registerUserInputPortResponse = new RegisterUserInputPortResponse(createUserAuthOutputPortResponse.getId(), createUserAuthOutputPortResponse.getUsername());
        return registerUserInputPortResponse;
    }
}
