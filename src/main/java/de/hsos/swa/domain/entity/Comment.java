package de.hsos.swa.domain.entity;

import io.vertx.codegen.annotations.Nullable;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDateTime;
import java.util.*;

// TODO: Validierung spezifischer (zB. Titellänge, Descriptionlänge, etc.)
public class Comment {
    @Valid
    private UUID id;
    @NotBlank
    private String text;

    @PastOrPresent
    private LocalDateTime createdAt;

    @Valid
    private User user;

    @Nullable
    private Comment parentComment;

    @NotNull
    private List<Comment> replies = new ArrayList<>();

    private boolean active;

    private List<Vote> votes = new ArrayList<>();

    public Comment(LocalDateTime createdAt, User user, String text) {
        this.id = UUID.randomUUID();
        this.createdAt = createdAt;
        this.user = user;
        this.text = text;
        this.active = true;
    }

    public Comment(UUID id, LocalDateTime createdAt, User user, String text) {
        this.id = id;
        this.createdAt = createdAt;
        this.user = user;
        this.text = text;
        this.active = true;
    }

    public Comment(LocalDateTime createdAt, User user, String text, boolean active) {
        this.id = UUID.randomUUID();
        this.createdAt = createdAt;
        this.user = user;
        this.text = text;
        this.active = active;
    }

    public Comment(UUID id, LocalDateTime createdAt, User user, String text, boolean active) {
        this.id = id;
        this.createdAt = createdAt;
        this.user = user;
        this.text = text;
        this.active = active;
    }

    // GETTER
    public UUID getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public String getText() {
        if (isActive()) {
            return text;
        }
        return "<DELETED>";
    }

    public List<Comment> getReplies() {
        return replies;
    }

    public Comment getParentComment() {
        return this.parentComment;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }


    public void setText(String text) {
        this.text = text;
    }

    // COMMENTS
    // TODO: in Comment Service auslagern?
    public void addReply(Comment reply) {
        reply.parentComment = this;
        this.replies.add(reply);
    }

    // DELETION
    public boolean isActive() {
        return active;
    }

    public void disable() {
        this.active = false;
    }


    public List<Vote> getVotes() {
        return votes;
    }

    public void setVotes(List<Vote> votes) {
        this.votes = votes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Comment comment)) return false;
        return getId().equals(comment.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }


    public Integer getDownvotes() {
        // TODO: Implementieren
        return 10;
    }

    public Integer getUpvotes() {
        // TODO: Implementieren
        return 10;
    }

    public void addVote(Vote vote) {
        this.votes.add(vote);
    }
}
