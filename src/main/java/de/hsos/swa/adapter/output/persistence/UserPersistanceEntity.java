package de.hsos.swa.adapter.output.persistence;

import javax.persistence.*;
import java.util.UUID;

@Entity(name = "User")
@Table(name = "user_table")
@NamedQuery(name = "UserPersistanceEntity.findByUsername", query = "SELECT u FROM User u WHERE u.name = :username")
public class UserPersistanceEntity {
    @Id
    @GeneratedValue()
    UUID id;

    @Column(name = "user_name")
    String name;

    public UserPersistanceEntity() {
    }

    public UserPersistanceEntity(String name) {
        this.name = name;
    }

    public UserPersistanceEntity(UUID id, String name) {
        this.id = id;
        this.name = name;
    }
}
