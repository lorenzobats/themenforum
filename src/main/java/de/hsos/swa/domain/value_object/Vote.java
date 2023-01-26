package de.hsos.swa.domain.value_object;

import de.hsos.swa.domain.entity.User;

import java.util.Objects;

public class Vote {


    private User user;

    private VoteType voteType;

    public Vote(User user, VoteType voteType) {
        this.user = user;
        this.voteType = voteType;
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
