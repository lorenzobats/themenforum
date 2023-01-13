package de.hsos.swa.application.port.output.createUserAuth;

public interface CreateUserAuthOutputPort {
    CreateUserAuthResult createUserAuth(CreateUserAuthCommand command);
}
