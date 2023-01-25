package de.hsos.swa.infrastructure.persistence.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import de.hsos.swa.domain.entity.Comment;
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


    // TODO: Hier vllt besser die Strings (IDs) Speichern?
    @OneToMany(
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    List<CommentPersistenceModel> upvotedComments = new ArrayList<>();

    @OneToMany(
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    List<CommentPersistenceModel> downvotedComments = new ArrayList<>();


    @OneToMany(
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    List<PostPersistenceModel> upvotedPosts = new ArrayList<>();

    @OneToMany(
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    List<PostPersistenceModel> downvotedPosts = new ArrayList<>();

    public UserPersistenceModel() {
    }

    public UserPersistenceModel(String name) {
        this.name = name;
    }

    public UserPersistenceModel(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    public UserPersistenceModel(UUID id, String name, List<CommentPersistenceModel> upvotedComments, List<CommentPersistenceModel> downvotedComments, List<PostPersistenceModel> upvotedPosts, List<PostPersistenceModel> downvotedPosts) {
        this.id = id;
        this.name = name;
        this.upvotedComments = upvotedComments;
        this.downvotedComments = downvotedComments;
        this.upvotedPosts = upvotedPosts;
        this.downvotedPosts = downvotedPosts;
    }

    public static class Converter {
        public static User toDomainEntity(UserPersistenceModel userPersistenceModel) {
            return new User(userPersistenceModel.id, userPersistenceModel.name);
        }

        public static UserPersistenceModel toPersistenceModel(User user) {
            // TODO: vllt hier nur die IDs als Liste von Strings speichern? Ebenfalls in Domain entity?
            List<CommentPersistenceModel> upVotedComments = user.getUpvotedComments().stream().map(CommentPersistenceModel.Converter::toPersistenceModel).toList();
            List<CommentPersistenceModel> downVotedComments = user.getDownvotedComments().stream().map(CommentPersistenceModel.Converter::toPersistenceModel).toList();
            List<PostPersistenceModel> upVotedPosts = user.getUpvotedPosts().stream().map(PostPersistenceModel.Converter::toPersistenceModel).toList();
            List<PostPersistenceModel> downVotedPosts = user.getDownvotedPosts().stream().map(PostPersistenceModel.Converter::toPersistenceModel).toList();
            return new UserPersistenceModel(user.getId(), user.getName(), upVotedComments, downVotedComments, upVotedPosts, downVotedPosts);
        }
    }
}
