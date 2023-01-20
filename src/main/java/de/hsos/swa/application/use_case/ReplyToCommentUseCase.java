package de.hsos.swa.application.use_case;

import de.hsos.swa.application.port.input.Result;
import de.hsos.swa.application.port.input.ReplyToCommentInputPort;
import de.hsos.swa.application.port.input.request.ReplyToCommentInputPortRequest;
import de.hsos.swa.application.port.output.PostRepository;
import de.hsos.swa.application.port.output.UserRepository;
import de.hsos.swa.domain.entity.Comment;
import de.hsos.swa.domain.entity.Post;
import de.hsos.swa.domain.entity.User;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.UUID;

@ApplicationScoped
public class ReplyToCommentUseCase implements ReplyToCommentInputPort {
    @Inject
    Logger log;

    @Inject
    PostRepository postRepository;

    @Inject
    UserRepository userRepository;


    @Override
    public Result<UUID> replyToComment(ReplyToCommentInputPortRequest request) {
        Result<User> getUserResponse = this.userRepository.getUserByName(request.getUsername());

        if (!getUserResponse.isSuccessful()) {
            return Result.error("User does not exist"); // TODO: Error sinnvoll von Applicaion weiterleiten und differenzieren
        }

        User user = getUserResponse.getData();

        Result<Post> getPostResponse = this.postRepository.getPostById(UUID.fromString(request.getPostId()), true);
        if (!getPostResponse.isSuccessful()) {
            return Result.error("Post does not exist"); // TODO: Error sinnvoll von Applicaion weiterleiten und differenzieren
        }

        Post post = getPostResponse.getData();

        post.addReplyToComment(request.getCommentId(), new Comment(user, request.getCommentText()));


        Result<UUID> updatePostResponse = this.postRepository.updatePost(post);

        if (updatePostResponse.isSuccessful()) {
            return Result.success(updatePostResponse.getData());
        }

        return Result.error("Something went wrong " + updatePostResponse.getErrorMessage());

    }
}
