package de.hsos.swa.application.service;

import de.hsos.swa.application.port.input.registerUser.RegisterUserCommand;
import de.hsos.swa.application.port.input.registerUser.RegisterUserResult;
import de.hsos.swa.application.port.input.registerUser.RegisterUserUseCase;
import de.hsos.swa.application.port.output.createUser.CreateUserCommand;
import de.hsos.swa.application.port.output.createUser.CreateUserOutputPort;
import de.hsos.swa.application.port.output.createUser.CreateUserResult;
import de.hsos.swa.application.port.output.createUserAuth.CreateUserAuthCommand;
import de.hsos.swa.application.port.output.createUserAuth.CreateUserAuthOutputPort;
import de.hsos.swa.application.port.output.createUserAuth.CreateUserAuthResult;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class RegisterUserService implements RegisterUserUseCase {

    @Inject
    CreateUserOutputPort createUserOutputPort;

    @Inject
    CreateUserAuthOutputPort createUserAuthOutputPort;


    @Override
    public RegisterUserResult registerUser(RegisterUserCommand command) {
        // 1. User Entity erzeugen
        CreateUserCommand createUserCommand = new CreateUserCommand(
                command.getUsername()
        );
        CreateUserResult createUserResult = this.createUserOutputPort.createUser(createUserCommand);

        // 2. User Auth erzeugen
        if(createUserResult != null) {
            CreateUserAuthCommand  createUserAuthCommand = new CreateUserAuthCommand(
                    command.getUsername(),
                    command.getPassword(),
                    "member",
                    createUserResult.getId()
            );
            CreateUserAuthResult createUserAuthResult = this.createUserAuthOutputPort.createUserAuth(createUserAuthCommand);
            RegisterUserResult registerUserResult = new RegisterUserResult(createUserAuthResult.getId(), createUserAuthResult.getUsername());
            return registerUserResult;
        }
        return null;
    }
}
