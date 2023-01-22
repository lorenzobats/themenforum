package de.hsos.swa.domain.service;

import de.hsos.swa.domain.entity.Comment;
import de.hsos.swa.domain.entity.Post;
import de.hsos.swa.domain.entity.User;
import de.hsos.swa.domain.vo.Vote;
import de.hsos.swa.domain.vo.VoteType;

import javax.enterprise.context.ApplicationScoped;

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

    public void voteComment(Comment comment, User user, VoteType voteType) {
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
