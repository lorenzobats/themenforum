package de.hsos.swa.application.service.command;

import de.hsos.swa.application.annotations.ApplicationService;
import de.hsos.swa.application.input.command.RegisterUserUseCase;
import de.hsos.swa.application.input.dto.in.RegisterUserCommand;
import de.hsos.swa.application.input.dto.out.ApplicationResult;
import de.hsos.swa.application.output.auth.AuthorizationGateway;
import de.hsos.swa.application.output.auth.dto.out.RegisterAuthUserCommand;
import de.hsos.swa.application.output.repository.UserRepository;
import de.hsos.swa.application.output.repository.dto.in.RepositoryResult;
import de.hsos.swa.domain.entity.User;
import de.hsos.swa.domain.factory.UserFactory;
import de.hsos.swa.application.output.auth.dto.in.AuthorizationResult;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;


/**
 * Die UseCase Klasse RegisterUserService implementiert das Interface
 * RegisterUserUseCase der Boundary des Application Hexagons.
 * Es realisiert die Applikationslogik für das Deaktivieren eines Users durch einen Admin
 *
 * @author Oliver Schlüter
 * @author Lorenzo Battiston
 * @version 1.0
 * @see RegisterUserUseCase             Korrespondierende Input-Port für diesen Use Case
 * @see de.hsos.swa.application.input.dto.in.RegisterUserCommand             Korrespondierende Request DTO für diesen Use Case
 * @see UserRepository                  Output-Port zum Speichern des Forum-Nutzers
 * @see AuthorizationGateway            Output-Port zum Registrieren eines Users (Zugangsdaten speichern)
 */
@RequestScoped
@Transactional(Transactional.TxType.REQUIRES_NEW)
@ApplicationService
public class RegisterUserService implements RegisterUserUseCase {

    @Inject
    UserRepository userRepository;
    @Inject
    AuthorizationGateway authorizationGateway;


    /**
     * Erstellt einen neuen Nutzer und registriert diesen auf Basis der übergebenen Informationen.
     *
     * @param command           enthält Nutzernamen und Passwort des zu registrierenden Nutzers
     * @return ApplicationResult<User> enthält erstellten Nutzer bzw. Fehlermeldung bei Misserfolg
     */
    @Override
    public ApplicationResult<User> registerUser(RegisterUserCommand command) {
        RepositoryResult<User> existingUserResult = this.userRepository.getUserByName(command.username());
        if (existingUserResult.ok()){
            return ApplicationResult.notValid(command.username() + " is already uses as a username");
        }

        User user = UserFactory.createUser(command.username());
        RegisterAuthUserCommand createUserAuthRequest = new RegisterAuthUserCommand(
                command.username(),
                command.password(),
                "member",
                user.getId());

        AuthorizationResult<Void> registration = this.authorizationGateway
                .registerUser(createUserAuthRequest);
        if (registration.denied())
            return ApplicationResult.exception("Registration failed");


        RepositoryResult<User> createUserResponse = this.userRepository.saveUser(user);
        if (createUserResponse.error())
            return ApplicationResult.exception("Registration failed");

        return ApplicationResult.ok(createUserResponse.get());
    }
}
