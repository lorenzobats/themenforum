package de.hsos.swa.domain.entity;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class Vote {

    private UUID id;

    private User user;

    private VoteType voteType;

    private LocalDateTime createdAt;

    public Vote(User user, VoteType voteType) {
        this.id = UUID.randomUUID();
        this.user = user;
        this.voteType = voteType;
    }

    public Vote(UUID id, User user, VoteType voteType) {
        this.id = id;
        this.user = user;
        this.voteType = voteType;
    }

    public UUID getId() {
        return id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public User getUser() {
        return user;
    }

    public VoteType getVoteType() {
        return voteType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vote vote = (Vote) o;
        return Objects.equals(user, vote.user) && voteType == vote.voteType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, voteType);
    }
}
