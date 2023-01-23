package de.hsos.swa.domain.entity;

import de.hsos.swa.domain.value_object.Vote;
import de.hsos.swa.domain.value_object.VoteType;
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

    @NotNull
    private final Map<UUID, Vote> votes = new HashMap<>();

    public Comment(UUID id, LocalDateTime createdAt, User user, String text) {
        this.id = id;
        this.createdAt = createdAt;
        this.user = user;
        this.text = text;
    }

    public Comment(UUID id, LocalDateTime createdAt, User user, String text, List<Comment> replies) {
        this.id = id;
        this.createdAt = createdAt;
        this.user = user;
        this.text = text;
        this.replies = replies;
    }


    public Comment(LocalDateTime createdAt, User user, String text) {
        this.id = UUID.randomUUID();
        this.createdAt = createdAt;
        this.user = user;
        this.text = text;
    }


    // GETTER
    public UUID getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public String getText() {
        return text;
    }

    public List<Comment> getReplies() {
        return replies;
    }

    public Comment getParentComment() {
        return this.parentComment;
    }

    public Map<UUID, Vote> getVotes() {
        return votes;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    // COMMENTS
    // TODO: in Comment Service auslagern?
    public void addReply(Comment reply) {
        reply.parentComment = this;
        this.replies.add(reply);
    }


    // VOTES
    public void setVote(Vote vote) {
        this.votes.put(vote.getUser().getId(), vote);
    }

    public void removeVote(UUID userId) {
        this.votes.remove(userId);
    }

    public Integer getDownVotes() {
        int voting = 0;
        for (Vote v : this.votes.values()) {
            voting += (v.getVoteType().equals(VoteType.DOWN) ? 1 : 0);
        }
        return voting;
    }

    public Integer getUpVotes() {
        int voting = 0;
        for (Vote v : this.votes.values()) {
            voting += (v.getVoteType().equals(VoteType.UP) ? 1 : 0);
        }
        return voting;
    }
}
