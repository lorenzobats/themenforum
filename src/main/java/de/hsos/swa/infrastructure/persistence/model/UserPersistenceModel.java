package de.hsos.swa.infrastructure.persistence.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import de.hsos.swa.domain.entity.User;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity(name = "User")
@Table(name = "user_table")
@NamedQuery(name = "UserPersistenceModel.findByUsername", query = "SELECT u FROM User u WHERE u.name = :username")
@NamedQuery(name = "UserPersistenceModel.findById", query = "SELECT u FROM User u WHERE u.id = :id")
public class UserPersistenceModel {
    @Id
    UUID id;

    @Column(name = "user_name")
    String name;

    @OneToMany(
            mappedBy = "userPersistenceModel",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    @JsonBackReference
    List<TopicPersistenceModel> topics = new ArrayList<>();

    @OneToMany(
            mappedBy = "userPersistenceModel",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    @JsonBackReference
    List<PostPersistenceModel> posts = new ArrayList<>();

    @OneToMany(
            mappedBy = "userPersistenceModel",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    @JsonBackReference
    List<CommentPersistenceModel> comments = new ArrayList<>();

    @OneToMany(
            mappedBy = "userPersistenceModel",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    @JsonBackReference
    List<CommentVotePersistenceModel> commentVotes = new ArrayList<>();

    @OneToMany(
            mappedBy = "userPersistenceModel",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    @JsonBackReference
    List<PostVotePersistenceModel> postVotes = new ArrayList<>();

    public UserPersistenceModel() {
    }

    public UserPersistenceModel(String name) {
        this.name = name;
    }

    public UserPersistenceModel(UUID id, String name) {
        this.id = id;
        this.name = name;
    }


    public static class Converter {
        public static User toDomainEntity(UserPersistenceModel userPersistenceModel) {
            return new User(userPersistenceModel.id, userPersistenceModel.name);
        }

        public static UserPersistenceModel toPersistenceModel(User user) {
            return new UserPersistenceModel(user.getId(), user.getName());
        }
    }
}
