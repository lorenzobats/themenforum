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


    @Basic
    boolean active;

    public UserPersistenceModel() {
    }

    public UserPersistenceModel(UUID id, String name, boolean isActive) {
        this.id = id;
        this.name = name;
        this.active = isActive;
    }
    public static class Converter {
        public static User toDomainEntity(UserPersistenceModel persistenceModel) {
            return new User(persistenceModel.id, persistenceModel.name, persistenceModel.active);
        }

        public static UserPersistenceModel toPersistenceModel(User domainEntity) {
            return new UserPersistenceModel(domainEntity.getId(), domainEntity.getUsername(), domainEntity.isActive());
        }
    }
}
