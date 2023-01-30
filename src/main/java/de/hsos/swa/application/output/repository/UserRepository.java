package de.hsos.swa.application.output.repository;

import de.hsos.swa.application.util.Result;
import de.hsos.swa.domain.entity.User;

public interface UserRepository {
    // CREATE
    Result<User> saveUser(User user);


    // READ
    // TODO: getAllUsers
    Result<User> getUserById(String userId);

    Result<User> getUserByName(String username);

    Result<Boolean> isUserNameAvailable(String username);

    // UPDATE
    Result<User> updateUser(User user);

    // DELETE
    // TODO DeleteUser
}
