package de.hsos.swa.domain.entity;

import de.hsos.swa.domain.vo.VoteType;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class Vote {

    private UUID id;

    private User user;

    private VoteType voteType;

    private LocalDateTime createdAt;

    public Vote(User user, VoteType voteType) {
        this.createdAt = LocalDateTime.now();
        this.id = UUID.randomUUID();
        this.user = user;
        this.voteType = voteType;
    }

    public Vote(UUID id, User user, VoteType voteType, LocalDateTime createdAt) {
        this.createdAt = createdAt;
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

    public void setVoteType(VoteType voteType) {
        this.voteType = voteType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vote vote)) return false;
        return getId().equals(vote.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
