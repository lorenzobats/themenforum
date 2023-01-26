package de.hsos.swa.domain.entity;


import de.hsos.swa.domain.value_object.Vote;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.*;

public class User {
    private UUID id;

    @NotBlank
    @Size(min = 4, max = 20, message = "Username must be between 4 and 20 characters")
    private String name;


    private Set<Post> upvotedPosts = new HashSet<>();

    private Set<Post> downvotedPosts = new HashSet<>();

    private Set<Comment> upvotedComments = new HashSet<>();

    private Set<Comment> downvotedComments = new HashSet<>();

    public User(String name) {
        this.id = UUID.randomUUID();
        this.name = name;
    }

    public User(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    public UUID getId() {
        return this.id;
    }

    public String getName() {
        return name;
    }

    //ADD VOTED
    public boolean addUpvotePost(Post post) {
        return this.upvotedPosts.add(post);
    }

    public boolean addDownvotePost(Post post) {
        return this.downvotedPosts.add(post);
    }

    public boolean addUpvoteComment(Comment comment) {
        return this.upvotedComments.add(comment);
    }

    public boolean addDownvoteComment(Comment comment) {
        return this.downvotedComments.add(comment);
    }


    //REMOVE VOTED
    public boolean removeUpvotePost(Post post) {
        return this.upvotedPosts.remove(post);
    }

    public boolean removeDownvotePost(Post post) {
        return this.downvotedPosts.remove(post);
    }

    public boolean removeUpvoteComment(Comment comment) {
        return this.upvotedComments.remove(comment);
    }

    public boolean removeDownvoteComment(Comment comment) {
        return this.downvotedComments.remove(comment);
    }


    public Set<Post> getUpvotedPosts() {
        return upvotedPosts;
    }

    public Set<Post> getDownvotedPosts() {
        return downvotedPosts;
    }

    public Set<Comment> getUpvotedComments() {
        return upvotedComments;
    }

    public Set<Comment> getDownvotedComments() {
        return downvotedComments;
    }

    public void setUpvotedPosts(Set<Post> upvotedPosts) {
        this.upvotedPosts = upvotedPosts;
    }

    public void setDownvotedPosts(Set<Post> downvotedPosts) {
        this.downvotedPosts = downvotedPosts;
    }

    public void setUpvotedComments(Set<Comment> upvotedComments) {
        this.upvotedComments = upvotedComments;
    }

    public void setDownvotedComments(Set<Comment> downvotedComments) {
        this.downvotedComments = downvotedComments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(name, user.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
