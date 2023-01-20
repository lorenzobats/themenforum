package de.hsos.swa.application.use_case;

import de.hsos.swa.application.port.input.Result;
import de.hsos.swa.application.port.input.CommentPostInputPort;
import de.hsos.swa.application.port.input.request.CommentPostInputPortRequest;
import de.hsos.swa.application.port.output.PostRepository;
import de.hsos.swa.application.port.output.UserRepository;
import de.hsos.swa.domain.entity.Comment;
import de.hsos.swa.domain.entity.Post;
import de.hsos.swa.domain.entity.User;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.UUID;

@ApplicationScoped
public class CommentPostUseCase implements CommentPostInputPort {

    @Inject
    PostRepository postRepository;

    @Inject
    UserRepository userRepository;


    @Override
    public Result<UUID> commentPost(CommentPostInputPortRequest command) {
        Result<User> userResult = this.userRepository.getUserByName(command.getUsername());

        if (!userResult.isSuccessful()) {
            return Result.error("User does not exist"); // TODO: Error sinnvoll von Applicaion weiterleiten und differenzieren
        }

        User user = userResult.getData();

        Comment comment = new Comment(user, command.getCommentText());

        Result<Post> postResult = this.postRepository.getPostById(UUID.fromString(command.getPostId()), true);
        if (!postResult.isSuccessful()) {
            return Result.error("Post does not exist"); // TODO: Error sinnvoll von Applicaion weiterleiten und differenzieren
        }

        Post post = postResult.getData();

        post.addComment(comment);

        Result<UUID> commentResult = this.postRepository.updatePost(post);

        if (commentResult.isSuccessful()) {
            return Result.success(commentResult.getData());
        }

        return Result.error("Something went wrong " + commentResult.getErrorMessage());
    }
}
