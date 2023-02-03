package de.hsos.swa.infrastructure.authorization.model;

import javax.persistence.*;

import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.security.jpa.Password;
import io.quarkus.security.jpa.Roles;
import io.quarkus.security.jpa.UserDefinition;
import io.quarkus.security.jpa.Username;

import java.util.UUID;

@Entity
@Table(name = "user_table")
@UserDefinition
@NamedQuery(name = "UserAuthEntity.findRoleByUserId", query = "SELECT role FROM AuthUser WHERE userId = :userId")
@NamedQuery(name = "AuthUser.findRoleByUserName", query = "SELECT role FROM AuthUser WHERE userId = :userId")
public class AuthUser {
    @Id
    @GeneratedValue()
    UUID id;

    @Username
    @Column(unique = true)
    String username;

    @Password
    String password;

    @Roles
    String role;

    @Column(name = "user_id")
    UUID userId;

    public AuthUser() {
    }

    public String getUsername() {
        return username;
    }

    public AuthUser(String username, String password, String role, UUID userId) {
        this.username = username;
        this.password = BcryptUtil.bcryptHash(password);
        this.role = role;
        this.userId = userId;
    }
}
