package de.hsos.swa.domain.service;

import de.hsos.swa.domain.entity.*;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Optional;

@ApplicationScoped
public class VoteService {

    @Inject
    Logger log;

    public void votePost(Post post, User user, VoteType voteType) {

        //Wenn es sich nicht um ein Post des Users handelt
        if (!post.getCreator().getId().equals(user.getId())) {
            Vote vote = new Vote(user, voteType);
            // TODO: ergaenzen
            post.addVote(vote);
        }
    }

    public void voteComment(Post post, User user, String commentId, VoteType voteType) {
        Optional<Comment> optionalComment = post.findCommentById(commentId);
        if (optionalComment.isEmpty()) {
            throw new RuntimeException("Comment " + commentId + " cannot be found within Post + " + post.getId());
        }
        Comment comment = optionalComment.get();

        //Wenn es sich nicht um ein Comment des Users handelt
        if (!comment.getUser().getId().equals(user.getId())) {
            Vote vote = new Vote(user, voteType);
            // TODO: ergaenzen
            comment.addVote(vote);
        }
    }
}
