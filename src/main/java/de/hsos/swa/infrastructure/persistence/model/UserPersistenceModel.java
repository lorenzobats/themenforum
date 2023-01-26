package de.hsos.swa.infrastructure.persistence.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import de.hsos.swa.domain.entity.Comment;
import de.hsos.swa.domain.entity.Post;
import de.hsos.swa.domain.entity.User;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Entity(name = "User")
@Table(name = "user_table")
@NamedQuery(name = "UserPersistenceModel.findByUsername", query = "SELECT u FROM User u WHERE u.name = :username")
@NamedQuery(name = "UserPersistenceModel.findById", query = "SELECT u FROM User u WHERE u.id = :id")
public class UserPersistenceModel {
    @Id
    UUID id;

    @Column(name = "user_name")
    String name;

    @ManyToMany(
            cascade=CascadeType.MERGE
    )
    @JoinTable(
            name = "comment_upvotes",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "comment_id"))
    Set<CommentPersistenceModel> upvotedComments = new HashSet<>();

    @ManyToMany(
            cascade=CascadeType.MERGE
    )
    @JoinTable(
            name = "comment_downvotes",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "comment_id"))
    Set<CommentPersistenceModel> downvotedComments = new HashSet<>();

    @ManyToMany(
            cascade=CascadeType.MERGE
    )
    @JoinTable(
            name = "post_upvotes",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "post_id"))
    Set<PostPersistenceModel> upvotedPosts = new HashSet<>();

    @ManyToMany(
            cascade=CascadeType.MERGE
    )
    @JoinTable(
            name = "post_downvotes",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "post_id"))
    Set<PostPersistenceModel> downvotedPosts = new HashSet<>();

    public UserPersistenceModel() {
    }


    public UserPersistenceModel(UUID id, String name, Set<PostPersistenceModel> downvotedPosts, Set<PostPersistenceModel> upvotedPosts, Set<CommentPersistenceModel> downvotedComments, Set<CommentPersistenceModel> upvotedComments) {
        this.id = id;
        this.name = name;
        this.downvotedPosts = downvotedPosts;
        this.upvotedPosts = upvotedPosts;
    }

    public static class Converter {
        public static User toDomainEntity(UserPersistenceModel userPersistenceModel) {
            Set<Post> downVotedPosts = userPersistenceModel.downvotedPosts.stream().map(PostPersistenceModel.Converter::toDomainEntity).collect(Collectors.toSet());
            Set<Post> upVotedPosts = userPersistenceModel.upvotedPosts.stream().map(PostPersistenceModel.Converter::toDomainEntity).collect(Collectors.toSet());
            Set<Comment> downVotedComments = userPersistenceModel.downvotedComments.stream().map(CommentPersistenceModel.Converter::toDomainEntity).collect(Collectors.toSet());
            Set<Comment> upVotedComments = userPersistenceModel.upvotedComments.stream().map(CommentPersistenceModel.Converter::toDomainEntity).collect(Collectors.toSet());

            User user = new User(userPersistenceModel.id, userPersistenceModel.name);

            user.setDownvotedPosts(downVotedPosts);
            user.setUpvotedPosts(upVotedPosts);
            user.setDownvotedComments(downVotedComments);
            user.setUpvotedComments(upVotedComments);
            return user;
        }

        public static UserPersistenceModel toPersistenceModel(User user) {
            Set<PostPersistenceModel> upVotedPosts = user.getUpvotedPosts().stream().map(PostPersistenceModel.Converter::toPersistenceModel).collect(Collectors.toSet());
            Set<PostPersistenceModel> downVotedPosts = user.getDownvotedPosts().stream().map(PostPersistenceModel.Converter::toPersistenceModel).collect(Collectors.toSet());
            Set<CommentPersistenceModel> upVotedComments = user.getUpvotedComments().stream().map(CommentPersistenceModel.Converter::toPersistenceModel).collect(Collectors.toSet());
            Set<CommentPersistenceModel> downVotedComments = user.getDownvotedComments().stream().map(CommentPersistenceModel.Converter::toPersistenceModel).collect(Collectors.toSet());
            return new UserPersistenceModel(user.getId(), user.getName(), downVotedPosts, upVotedPosts, downVotedComments, upVotedComments);
        }
    }
}
