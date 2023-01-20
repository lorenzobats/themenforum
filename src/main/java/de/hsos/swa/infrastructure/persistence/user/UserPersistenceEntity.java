package de.hsos.swa.infrastructure.persistence.user;

import de.hsos.swa.domain.entity.User;

import javax.persistence.*;
import java.util.UUID;

@Entity(name = "User")
@Table(name = "user_table")
@NamedQuery(name = "UserPersistenceEntity.findByUsername", query = "SELECT u FROM User u WHERE u.name = :username")
@NamedQuery(name = "UserPersistenceEntity.findById", query = "SELECT u FROM User u WHERE u.id = :id")
public class UserPersistenceEntity {
    @Id
    UUID id;

    @Column(name = "user_name")
    String name;

    public UserPersistenceEntity() {
    }

    public UserPersistenceEntity(String name) {
        this.name = name;
    }

    public UserPersistenceEntity(UUID id, String name) {
        this.id = id;
        this.name = name;
    }


    public static class Converter {
        public static User toDomainEntity(UserPersistenceEntity userPersistenceEntity) {
            return new User(userPersistenceEntity.id, userPersistenceEntity.name);
        }

        public static UserPersistenceEntity toPersistenceEntity(User user) {
            return new UserPersistenceEntity(user.getId(), user.getName());
        }
    }
}
