package de.hsos.swa.control.dto;

import de.hsos.swa.entity.User;

public class UserRegistrationDto {
    public String username;
    public String password;

    public UserRegistrationDto() {
    }

    public UserRegistrationDto(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public static class Converter {
        public static User toEntity(UserRegistrationDto userRegistrationDto) {
            return new User(userRegistrationDto.username);
        }
    }
}
