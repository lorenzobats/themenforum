package de.hsos.swa.domain.service;

import de.hsos.swa.domain.entity.Comment;
import de.hsos.swa.domain.entity.Post;
import de.hsos.swa.domain.entity.User;
import de.hsos.swa.domain.value_object.VoteType;
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
            switch (voteType) {
                case UP:
                    if (user.addUpvotePost(post)) {
                        post.addUpvote();
                    }
                    if (user.removeDownvotePost(post)) {
                        post.removeDownvote();
                    }
                    break;
                case DOWN:
                    if (user.addDownvotePost(post)) {
                        post.addDownvote();
                    }
                    if (user.removeUpvotePost(post)) {
                        post.removeUpvote();
                    }
                    break;
                case NONE:
                    if (user.removeUpvotePost(post)) {
                        post.removeUpvote();
                    }
                    if (user.removeDownvotePost(post)) {
                        post.removeDownvote();
                    }
                    break;
            }
        }
    }

    public void voteComment(Post post, User user, String commentId, VoteType voteType) {
        Optional<Comment> optionalComment = post.findCommentById(commentId);
        if (optionalComment.isEmpty()) {
            throw new RuntimeException("Comment " + commentId + " cannot be found within Post + " + post.getId());
        }
        Comment comment = optionalComment.get();

        // TODO: up/down
        //Wenn es sich nicht um ein Comment des Users handelt
        if (!comment.getUser().getId().equals(user.getId())) {
            switch (voteType) {
                case UP:
                    if(user.addUpvoteComment(comment)){

                    }

                    user.removeDownvoteComment(comment);
                    break;
                case DOWN:
                    user.addDownvoteComment(comment);
                    user.removeUpvoteComment(comment);
                    break;
                case NONE:
                    user.removeUpvoteComment(comment);
                    user.removeDownvoteComment(comment);
                    break;
            }
        }
    }
}
