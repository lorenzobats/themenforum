package de.hsos.swa.control;

import de.hsos.swa.control.dto.UserRegistrationDto;
import de.hsos.swa.entity.User;
import de.hsos.swa.entity.repository.UserAuthRepository;
import de.hsos.swa.entity.repository.UserRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Optional;
import java.util.UUID;

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
        Optional<User> optionalUser = userRepository.addUser(UserRegistrationDto.Converter.toEntity(userRegistrationDto));
        if(optionalUser.isPresent()) {
            try {
                authRepository.registerUser(userRegistrationDto.username, userRegistrationDto.password, "member", optionalUser.get().getId().toString());
                return optionalUser;
            } catch (Exception e) {
                return Optional.empty();
            }
        }
        return Optional.empty();
    }

    public Optional<User> getUserByName(String username){
        return this.userRepository.getUserByName(username);
    }

    public Optional<User> getUserById(UUID id){
        return this.userRepository.getUserById(id);
    }

}
