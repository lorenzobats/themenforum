package de.hsos.swa.infrastructure.authorization.model;

import javax.persistence.*;
import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.security.jpa.Password;
import io.quarkus.security.jpa.Roles;
import io.quarkus.security.jpa.UserDefinition;
import io.quarkus.security.jpa.Username;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "auth_user_table", schema = "auth")
@UserDefinition
@NamedQuery(name = "AuthUser.findRoleByUserId", query = "SELECT role FROM AuthUser WHERE userId = :userId")
@NamedQuery(name = "AuthUser.findRoleByUserName", query = "SELECT role FROM AuthUser WHERE username = :username")
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

    @OneToMany(mappedBy = "owner", cascade = CascadeType.REMOVE)
    List<OwnerOf> ownedRessources;

    public AuthUser() {
    }

    public String getUsername() {
        return username;
    }

    public String getRole() {
        return role;
    }


    public AuthUser(String username, String password, String role, UUID userId) {
        this.username = username;
        this.password = BcryptUtil.bcryptHash(password);
        this.role = role;
        this.userId = userId;
    }
}
