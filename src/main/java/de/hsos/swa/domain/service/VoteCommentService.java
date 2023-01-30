package de.hsos.swa.domain.service;

import de.hsos.swa.domain.entity.*;

import javax.enterprise.context.ApplicationScoped;
import java.util.Optional;

@ApplicationScoped
public class VoteCommentService {

    public boolean voteComment(Post post, User user, String commentId, VoteType voteType) {
        Optional<Comment> optionalComment = post.findCommentById(commentId);
        if (optionalComment.isEmpty()) {
            throw new RuntimeException("Comment " + commentId + " cannot be found within Post + " + post.getId());
        }
        Comment comment = optionalComment.get();


        if(comment.getUser().getId().equals(user.getId())){
            return false;
        }

        Optional<Vote> optionalVote = comment.findVoteByUserId(user.getId());
        if (optionalVote.isPresent()) {
            Vote existingVote = optionalVote.get();

            if(voteType.equals(existingVote.getVoteType())){
                return false;
            }

            if(voteType.equals(VoteType.NONE)){
                comment.removeVote(existingVote);
                return true;
            }

            existingVote.setVoteType(voteType);
            return true;
        } else {
            Vote vote = new Vote(user, voteType);
            comment.addVote(vote);
        }
        return true;
    }
}
