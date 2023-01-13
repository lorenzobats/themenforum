package de.hsos.swa.entity.repository;

import de.hsos.swa.entity.User;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    Optional<User> addUser(User user);
    Optional<User> getUserById(UUID userId);

    Optional<User> getUserByName(String username);

    boolean usernameExists(String username);
}
