package de.hsos.swa.domain.service;

import de.hsos.swa.domain.entity.User;
import de.hsos.swa.domain.entity.Vote;
import de.hsos.swa.domain.vo.VoteType;
import de.hsos.swa.domain.entity.VotedEntity;

import javax.enterprise.context.RequestScoped;
import java.util.Optional;

@RequestScoped
public class VoteService {
    public Optional<Vote> vote(VotedEntity entity, User user, VoteType voteType) {
        // User kann seine eigenen Posts/Comments nicht voten
        if (entity.getUser().getId().equals(user.getId())) {
            return Optional.empty();
        }

        // Fall 1: Neuer Vote
        Optional<Vote> optionalVote = entity.findVoteByUser(user.getId());
        if (optionalVote.isEmpty()) {
            Vote vote = new Vote(user, voteType);
            entity.addVote(vote);
            return Optional.of(vote);
        }

        // Fall 2: Aenderung eines bestehenden Votes
        Vote oldVote = optionalVote.get();
        if (voteType != oldVote.getVoteType()) {
            entity.removeVote(oldVote);
            Vote newVote = new Vote(user, voteType);
            entity.addVote(newVote);
            return Optional.of(newVote);
        }

        return Optional.empty();
    }

    public Optional<Vote> deleteVote(VotedEntity entity, User user) {
        Optional<Vote> optionalVote = entity.findVoteByUser(user.getId());
        if (optionalVote.isEmpty()) {
            return Optional.empty();
        }

        entity.removeVote(optionalVote.get());
        return optionalVote;
    }
}
