package de.hsos.swa.control;

import de.hsos.swa.control.dto.UserRegistrationDto;
import de.hsos.swa.entity.User;
import de.hsos.swa.entity.repository.UserAuthRepository;
import de.hsos.swa.entity.repository.UserRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Optional;

@ApplicationScoped
public class UserManagement {

    @Inject
    UserRepository userRepository;

    @Inject
    UserAuthRepository authRepository;

    public boolean usernameExists(String username) {
        return userRepository.usernameExists(username);
    }

    public Optional<User> createUser(UserRegistrationDto userRegistrationDto) {
        Optional<User> user = userRepository.addUser(UserRegistrationDto.Converter.toEntity(userRegistrationDto));
        if(user.isPresent()) {
            authRepository.registerUser(userRegistrationDto);
        }
    }
}
