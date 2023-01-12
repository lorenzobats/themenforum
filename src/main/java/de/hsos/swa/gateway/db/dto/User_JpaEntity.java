package de.hsos.swa.gateway.db.dto;

import de.hsos.swa.entity.User;

import javax.persistence.*;
import java.util.UUID;

@Entity(name = "User")
@Table(name = "user_table")
public class User_JpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "user_seq")
    private UUID id;

    @Column(name = "user_name")
    private String name;

    public User_JpaEntity() {
    }

    public User_JpaEntity(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    public static class Converter {
        public static User toEntity(User_JpaEntity user_jpaEntity) {
            //TODO Factories???
            return new User(user_jpaEntity.id, user_jpaEntity.name);
        }

        public static User_JpaEntity toJpaEntity(User user) {
            return new User_JpaEntity(user.getId(), user.getName());
        }
    }
}
