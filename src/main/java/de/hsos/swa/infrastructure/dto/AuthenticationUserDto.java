package de.hsos.swa.infrastructure.dto;

import de.hsos.swa.infrastructure.Keycloak_User;

public class AuthenticationUserDto {
    public String username;

    public String password;

    public String role;

    public AuthenticationUserDto() {
    }

    public AuthenticationUserDto(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public static class Converter {
        public static Keycloak_User toKeycloakUser(AuthenticationUserDto userDto) {
            return new Keycloak_User(userDto.username, userDto.password, userDto.role, )
        }
    }
}
