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
        if (!post.getCreator().getId().equals(user.getId())) {
            Optional<Vote> optionalVote = post.findVoteByUserId(user.getId());
            if(optionalVote.isPresent()){
                optionalVote.get().setVoteType(voteType);
            } else {
                Vote vote = new Vote(user, voteType);
                post.addVote(vote);
            }
        }
    }

    public void voteComment(Post post, User user, String commentId, VoteType voteType) {
        Optional<Comment> optionalComment = post.findCommentById(commentId);
        if (optionalComment.isEmpty()) {
            throw new RuntimeException("Comment " + commentId + " cannot be found within Post + " + post.getId());
        }
        Comment comment = optionalComment.get();

        if (!comment.getUser().getId().equals(user.getId())) {
            Optional<Vote> optionalVote = comment.findVoteByUserId(user.getId());
            if(optionalVote.isPresent()){
                optionalVote.get().setVoteType(voteType);
            } else {
                Vote vote = new Vote(user, voteType);
                comment.addVote(vote);
            }
        }
    }
}
