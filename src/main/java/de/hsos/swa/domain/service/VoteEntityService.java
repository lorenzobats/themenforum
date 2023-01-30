package de.hsos.swa.domain.service;

import de.hsos.swa.domain.entity.User;
import de.hsos.swa.domain.entity.Vote;
import de.hsos.swa.domain.entity.VoteType;
import de.hsos.swa.domain.entity.VotedEntity;

import javax.enterprise.context.RequestScoped;
import java.util.Optional;

@RequestScoped
public class VoteEntityService {
    public Optional<Vote> vote(VotedEntity entity, User user, VoteType voteType) {
        if (entity.getUser().getId().equals(user.getId())) {
            //Kein Vote, da User nicht eigene Enities voten kann
            return Optional.empty();
        }

        //Fall der User einen bestehenden Vote aendert (UP->DOWN/DOWN->UP)
        Optional<Vote> optionalVote = entity.findVoteByUserId(user.getId());

        if (optionalVote.isEmpty()) {
            Vote vote = new Vote(user, voteType);
            entity.addVote(vote);
            return Optional.of(vote);
        }

        Vote existingVote = optionalVote.get();

        if (voteType != existingVote.getVoteType()) {
            existingVote.setVoteType(voteType);
            return Optional.of(existingVote);
        }

        if (voteType == VoteType.NONE) {
            entity.removeVote(existingVote);
            return Optional.of(existingVote);
        }

        return Optional.empty();
    }

    public Optional<Vote> deleteVote(VotedEntity entity, User user) {
        if (!entity.getUser().getId().equals(user.getId())) {
            //Kein Vote, da User nicht eigene Enities voten kann
            return Optional.empty();
        }

        Optional<Vote> optionalVote = entity.findVoteByUserId(user.getId());
        if (optionalVote.isEmpty()) {
            return Optional.empty();
        }

        entity.removeVote(optionalVote.get());
        return optionalVote;
    }
}
