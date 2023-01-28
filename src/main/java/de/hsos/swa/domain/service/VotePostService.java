package de.hsos.swa.domain.service;

import de.hsos.swa.domain.entity.*;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Optional;

@ApplicationScoped
public class VotePostService {

    @Inject
    Logger log;

    public void votePost(Post post, User user, VoteType voteType) {
        if (!post.getCreator().getId().equals(user.getId())) {
            Optional<Vote> optionalVote = post.findVoteByUserId(user.getId());
            if (optionalVote.isPresent()) {
                optionalVote.get().setVoteType(voteType);
            } else {
                Vote vote = new Vote(user, voteType);
                post.addVote(vote);
            }
        }
    }
}
