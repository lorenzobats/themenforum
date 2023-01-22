package de.hsos.swa.infrastructure.rest.dto.out;

import de.hsos.swa.domain.entity.User;

public class UserDto {

    public String id;
    public String username;


    public UserDto() {
    }

    public UserDto(String id, String username) {
        this.id = id;
        this.username = username;

    }

    public static class Converter {
        public static UserDto fromDomainEntity(User user) {
            return new UserDto(user.getId().toString(), user.getName());
        }
    }
}
