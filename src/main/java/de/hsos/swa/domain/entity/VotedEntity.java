package de.hsos.swa.domain.entity;

import java.util.Optional;
import java.util.UUID;

public interface VotedEntity {
    User getUser();

    Optional<Vote> findVoteByUserId(UUID userId);

    void removeVote(Vote vote);

    void addVote(Vote vote);
}
