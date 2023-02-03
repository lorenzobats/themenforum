package de.hsos.swa.domain.entity;

import de.hsos.swa.domain.vo.VoteType;
import io.vertx.codegen.annotations.Nullable;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDateTime;
import java.util.*;

public class Comment implements SortedEntity, VotedEntity {
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

    private boolean isActive;

    private List<Vote> votes = new ArrayList<>();

    //------------------------------------------------------------------------------------------------------------------
    // CONSTRUCTORS

    // Für Factory
    public Comment(LocalDateTime createdAt, User user, String text) {
        this.id = UUID.randomUUID();
        this.createdAt = createdAt;
        this.user = user;
        this.text = text;
        this.isActive = true;
    }

    // Für Persistence Adapter
    public Comment(UUID id, LocalDateTime createdAt, User user, String text, boolean isActive) {
        this.id = id;
        this.createdAt = createdAt;
        this.user = user;
        this.text = text;
        this.isActive = isActive;
    }


    //------------------------------------------------------------------------------------------------------------------
    // SIMPLE GETTER
    public boolean isActive() {
        return isActive;
    }
    public UUID getId() {
        return id;
    }

    public User getUser() {
        return this.user;
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

    public List<Vote> getVotes() {
        return votes;
    }



    //------------------------------------------------------------------------------------------------------------------
    // DELETE
    public void disable() {
        this.isActive = false;
    }


    //------------------------------------------------------------------------------------------------------------------
    // RELPLY
    public void addReply(Comment reply) {
        reply.parentComment = this;
        this.replies.add(reply);
    }

    //------------------------------------------------------------------------------------------------------------------
    // VOTE
    @Override
    public Optional<Vote> findVoteByUser(UUID userId) {
        for (Vote vote : this.votes) {
            if(vote.getUser().getId().equals(userId)){
                return Optional.of(vote);
            }
        }
        return Optional.empty();
    }

    public Integer getDownvotes() {
        int voting = 0;
        for (Vote v : this.votes){
            if(v.getVoteType().equals(VoteType.DOWN)){
                voting ++;
            }
        }
        return voting;
    }

    public Integer getUpvotes() {
        int voting = 0;
        for (Vote v : this.votes){
            if(v.getVoteType().equals(VoteType.UP)){
                voting ++;
            }
        }
        return voting;
    }

    public Integer getTotalVotes() {
        return this.getUpvotes() - this.getDownvotes();
    }

    public void addVote(Vote vote) {
        this.votes.add(vote);
    }

    public void removeVote(Vote vote) {
        this.votes.remove(vote);
    }


    //------------------------------------------------------------------------------------------------------------------
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

    public boolean isRootComment() {
        return this.parentComment == null;
    }
}
