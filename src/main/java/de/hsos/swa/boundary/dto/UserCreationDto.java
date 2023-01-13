package de.hsos.swa.boundary.dto;

import de.hsos.swa.control.dto.UserRegistrationDto;

public class UserCreationDto {

    public String username;
    public String password;

    public UserCreationDto() {
    }

    public UserCreationDto(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public static class Converter {
        public static UserRegistrationDto toRegistrationDto(UserCreationDto userCreationDto) {
            return new UserRegistrationDto(userCreationDto.username, userCreationDto.password);
        }
    }
}
