package de.hsos.swa.application.output.repository;

import de.hsos.swa.domain.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserRepository {
    // CREATE
    RepositoryResult<User> saveUser(User user);

    // READ
    RepositoryResult<List<User>> getAllUsers();
    RepositoryResult<User> getUserById(UUID userId);

    RepositoryResult<User> getUserByName(String username);

    // UPDATE
    RepositoryResult<User> updateUser(User user);
}
