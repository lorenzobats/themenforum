package de.hsos.swa.application.output.repository;

import de.hsos.swa.domain.entity.User;

import java.util.List;

public interface UserRepository {
    // CREATE
    RepositoryResult<User> saveUser(User user);

    // READ
    RepositoryResult<List<User>> getAllUsers();
    RepositoryResult<User> getUserById(String userId);

    RepositoryResult<User> getUserByName(String username);

    RepositoryResult<Boolean> existsUserWithName(String username);

    // UPDATE
    RepositoryResult<User> updateUser(User user);
}
