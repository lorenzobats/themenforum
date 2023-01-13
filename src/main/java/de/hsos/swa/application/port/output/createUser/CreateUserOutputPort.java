package de.hsos.swa.application.port.output.createUser;

public interface CreateUserOutputPort {
    CreateUserResult createUser(CreateUserCommand command);
}
