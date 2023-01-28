package de.hsos.swa.domain.service;

import de.hsos.swa.domain.entity.*;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Optional;

@ApplicationScoped
public class VotePostService {

    public boolean votePost(Post post, User user, VoteType voteType) {
        if(post.getCreator().getId().equals(user.getId())){
            return false;
        }
        Optional<Vote> optionalVote = post.findVoteByUserId(user.getId());
        if (optionalVote.isPresent()) {
            Vote existingVote = optionalVote.get();

            if(voteType.equals(existingVote.getVoteType())){
                return false;
            }

            if(voteType.equals(VoteType.NONE)){
                post.removeVote(existingVote);
                return true;
            }

            existingVote.setVoteType(voteType);
            return true;
        } else {
            Vote vote = new Vote(user, voteType);
            post.addVote(vote);
        }
        return true;
    }
}
