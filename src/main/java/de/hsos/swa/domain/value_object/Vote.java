package de.hsos.swa.domain.value_object;

import de.hsos.swa.domain.entity.User;

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
}
