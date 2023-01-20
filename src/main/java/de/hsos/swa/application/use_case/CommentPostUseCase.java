package de.hsos.swa.application.use_case;

import de.hsos.swa.application.Result;
import de.hsos.swa.application.input.CommentPostInputPort;
import de.hsos.swa.application.input.request.CommentPostInputPortRequest;
import de.hsos.swa.application.output.persistence.UserRepository;
import de.hsos.swa.application.output.persistence.PostRepository;
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
    public Result<Comment> commentPost(CommentPostInputPortRequest request) {
        Result<User> userResult = this.userRepository.getUserByName(request.getUsername());

        if (!userResult.isSuccessful()) {
            return Result.error("User does not exist"); // TODO: Error sinnvoll von Applicaion weiterleiten und differenzieren
        }

        User user = userResult.getData();

        Comment comment = new Comment(user, request.getCommentText());

        Result<Post> postResult = this.postRepository.getPostById(UUID.fromString(request.getPostId()), true);
        if (!postResult.isSuccessful()) {
            return Result.error("Post does not exist"); // TODO: Error sinnvoll von Applicaion weiterleiten und differenzieren
        }

        Post post = postResult.getData();

        post.addComment(comment);

        Result<Post> updatePostResult = this.postRepository.updatePost(post);

        if (updatePostResult.isSuccessful()) {
            return Result.success(comment);
        }

        return Result.error("Something went wrong " + updatePostResult.getErrorMessage());
    }
}
