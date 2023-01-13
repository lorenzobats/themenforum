package de.hsos.swa.infrastructure;

import javax.persistence.*;

import io.quarkus.security.jpa.Password;
import io.quarkus.security.jpa.Roles;
import io.quarkus.security.jpa.UserDefinition;
import io.quarkus.security.jpa.Username;

@Entity
@Table(name = "keycloack_user_table")
@UserDefinition
public class Keycloak_User {
    @Id
    @GeneratedValue(generator = "user_id_seq")
    @SequenceGenerator(name = "user_id_seq", sequenceName = "user_id_seq", allocationSize = 1, initialValue = 100)
    private Long id;

    @Username
    public String username;

    @Password
    public String password;

    @Roles
    public String role;

    public String userId;

    public Keycloak_User(String username, String password, String role, String userId) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.userId = userId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Id
    public Long getId() {
        return id;
    }
}
