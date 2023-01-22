package de.hsos.swa.domain.service;

import de.hsos.swa.domain.entity.Comment;
import de.hsos.swa.domain.entity.Post;
import de.hsos.swa.domain.entity.User;
import de.hsos.swa.domain.vo.Vote;
import de.hsos.swa.domain.vo.VoteType;

import javax.enterprise.context.ApplicationScoped;
import java.util.Optional;

@ApplicationScoped
public class VoteService {

    public void votePost(Post post, User user, VoteType voteType) {
        if(post.getCreator().getId().equals(user.getId())){
            throw new RuntimeException("Cannot Vote your Own Post");
        }
        if (voteType.equals(VoteType.NONE)) {
            post.removeVote(user.getId());
        } else {
            post.setVote(new Vote(user, voteType));
        }
    }

    public void voteComment(Post post, User user, String commentId, VoteType voteType) {
        Optional<Comment> optionalComment = post.findCommentById(commentId);
        if(optionalComment.isEmpty()){
            throw new RuntimeException("Comment " + commentId + " cannot be found within Post + " + post.getId() );
        }
        Comment comment = optionalComment.get();
        if(comment.getUser().getId().equals(user.getId())){
            throw new RuntimeException("Cannot Vote your Own Comment");
        }

        if (voteType.equals(VoteType.NONE)) {
            comment.removeVote(user.getId());
        } else {
            comment.setVote(new Vote(user, voteType));
        }
    }
}
