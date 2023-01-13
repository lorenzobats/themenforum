package de.hsos.swa.infrastructure.dto;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Username
    public String username;

    @Password
    public String password;

    @Roles
    public String role;

    public String userId;

    public Keycloak_User() {
    }

    public Keycloak_User(String username, String password, String role, String userId) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.userId = userId;
    }
}
