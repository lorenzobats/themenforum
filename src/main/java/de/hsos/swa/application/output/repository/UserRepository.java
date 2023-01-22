package de.hsos.swa.application.output.repository;

import de.hsos.swa.application.output.Result;
import de.hsos.swa.domain.entity.User;

public interface UserRepository {
    Result<Boolean> isUserNameAvailable(String username);

    Result<User> getUserById(String userId);

    Result<User> getUserByName(String username);

    Result<User> saveUser(User user);
}
