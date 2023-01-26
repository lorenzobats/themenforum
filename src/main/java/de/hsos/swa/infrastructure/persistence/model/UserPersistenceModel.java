package de.hsos.swa.infrastructure.persistence.model;

import de.hsos.swa.domain.entity.User;

import javax.persistence.*;
import java.util.*;

@Entity(name = "User")
@Table(name = "user_table")
@NamedQuery(name = "UserPersistenceModel.findByUsername", query = "SELECT u FROM User u WHERE u.name = :username")
@NamedQuery(name = "UserPersistenceModel.findById", query = "SELECT u FROM User u WHERE u.id = :id")
public class UserPersistenceModel {
    @Id
    UUID id;

    @Column(name = "user_name")
    String name;

    public UserPersistenceModel() {
    }


    public UserPersistenceModel(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    public static class Converter {
        public static User toDomainEntity(UserPersistenceModel userPersistenceModel) {
            User user = new User(userPersistenceModel.id, userPersistenceModel.name);
            return user;
        }

        public static UserPersistenceModel toPersistenceModel(User user) {
            return new UserPersistenceModel(user.getId(), user.getName());
        }
    }
}
