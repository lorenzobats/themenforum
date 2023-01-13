package de.hsos.swa.entity.repository;


import de.hsos.swa.control.dto.UserRegistrationDto;

public interface UserAuthRepository {
    boolean registerUser(UserRegistrationDto user);
}
