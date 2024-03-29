package de.hsos.swa.application.output.repository;

import de.hsos.swa.application.output.repository.dto.in.RepositoryResult;
import de.hsos.swa.domain.entity.User;

import java.util.List;
import java.util.UUID;
/**
 *
 */
public interface UserRepository {

    //------------------------------------------------------------------------------------------------------------------
    // COMMANDS
    RepositoryResult<User> saveUser(User user);
    RepositoryResult<User> updateUser(User user);

    //------------------------------------------------------------------------------------------------------------------
    // QUERIES
    RepositoryResult<List<User>> getAllUsers();
    RepositoryResult<User> getUserById(UUID userId);
    RepositoryResult<User> getUserByName(String username);
}
