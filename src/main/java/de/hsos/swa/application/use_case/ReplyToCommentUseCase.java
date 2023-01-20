package de.hsos.swa.application.use_case;

import de.hsos.swa.application.port.input._shared.Result;
import de.hsos.swa.application.port.input.replyToComment.ReplyToCommentInputPort;
import de.hsos.swa.application.port.input.replyToComment.ReplyToCommentInputPortRequest;
import de.hsos.swa.application.port.input.replyToComment.ReplyToCommentInputPortResponse;
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
    public Result<ReplyToCommentInputPortResponse> replyToComment(ReplyToCommentInputPortRequest request) {
        Result<User> getUserResponse = this.userRepository.getUserByName(request.getUsername());
        if (!getUserResponse.isSuccessful()) {
            return Result.error("User does not exist"); // TODO: Error sinnvoll von Applicaion weiterleiten und differenzieren
        }
        User user = getUserResponse.getData();

        // 3. Post holen
        // TODO: Frage ? -> Nicht Post Id übergeben und output port definieren, der zugehörigen Post statt über "getPostById" "getPostOfComment"
        Result<Post> getPostResponse = this.postRepository.getPostById(UUID.fromString(request.getPostId()), true);
        if (!getPostResponse.isSuccessful()) {
            return Result.error("Post does not exist"); // TODO: Error sinnvoll von Applicaion weiterleiten und differenzieren
        }
        Post post = getPostResponse.getData();

        post.addReplyToComment(request.getCommentId(), new Comment(user, request.getCommentText()));


        Result<UUID> updatePostResponse = this.postRepository.updatePost(post);

        if (updatePostResponse.isSuccessful()) {
            return Result.success(new ReplyToCommentInputPortResponse(updatePostResponse.getData()));
        }

        return Result.error("Something went wrong " + updatePostResponse.getErrorMessage());

    }
}
