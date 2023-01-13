package de.hsos.swa.entity.repository;

import de.hsos.swa.entity.User;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    public Optional<User> addUser(User user);
    public Optional<User> getUserById(UUID userId);

    public boolean usernameExists(String username);
}
