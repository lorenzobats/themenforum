package de.hsos.swa.entity.repository;


import de.hsos.swa.control.dto.UserRegistrationDto;

public interface UserAuthRepository {
    boolean registerUser(String name, String password, String role, String userId);
}
