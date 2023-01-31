package de.hsos.swa.infrastructure.persistence.model;

import de.hsos.swa.domain.entity.User;

import javax.persistence.*;
import java.util.*;

@Entity(name = "User")
@Table(name = "user_table")
public class UserPersistenceModel {
    @Id
    UUID id;

    @Column(name = "user_name", unique = true)
    String name;

    public UserPersistenceModel() {
    }


    public UserPersistenceModel(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    public static class Converter {
        public static User toDomainEntity(UserPersistenceModel persistenceModel) {
            User user = new User(persistenceModel.id, persistenceModel.name);
            return user;
        }

        public static UserPersistenceModel toPersistenceModel(User domainEntity) {
            return new UserPersistenceModel(domainEntity.getId(), domainEntity.getName());
        }
    }
}
