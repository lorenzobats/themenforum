package de.hsos.swa.infrastructure.auth;

import javax.persistence.*;

import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.security.jpa.Password;
import io.quarkus.security.jpa.Roles;
import io.quarkus.security.jpa.UserDefinition;
import io.quarkus.security.jpa.Username;

import java.util.UUID;

@Entity
@Table(name = "auth_user_table")        // TODO Anders Schema
@UserDefinition
@NamedQuery(name = "UserAuthEntity.findRoleByUserId", query = "SELECT role FROM UserAuthEntity WHERE userId = :userId")
public class UserAuthEntity {
    @Id
    @GeneratedValue()
    UUID id;

    @Username
    String username;

    @Password
    String password;

    @Roles
    String role;

    @Column(name = "user_id")
    UUID userId;

    public UserAuthEntity() {
    }

    public UserAuthEntity(String username, String password, String role, UUID userId) {
        this.username = username;
        this.password = BcryptUtil.bcryptHash(password);
        this.role = role;
        this.userId = userId;
    }
}
