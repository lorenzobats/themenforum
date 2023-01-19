package de.hsos.swa.application.port.output;

import de.hsos.swa.application.port.input._shared.Result;
import de.hsos.swa.domain.entity.User;

import java.util.UUID;

public interface UserRepository {
    Result<Boolean> isUserNameAvailable(String username);

    Result<User> getUserById(String userId);

    Result<User> getUserByName(String username);

    Result<UUID> saveUser(User user);
}
