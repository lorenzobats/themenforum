package de.hsos.swa.application.commands;

import de.hsos.swa.application.output.Result;
import de.hsos.swa.application.input.CommentPostInputPort;
import de.hsos.swa.application.input.dto.in.CommentPostInputPortRequest;
import de.hsos.swa.application.output.repository.UserRepository;
import de.hsos.swa.application.output.repository.PostRepository;
import de.hsos.swa.domain.entity.Comment;
import de.hsos.swa.domain.entity.Post;
import de.hsos.swa.domain.entity.User;
import de.hsos.swa.domain.factory.CommentFactory;

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

        Comment comment = CommentFactory.createComment(request.getCommentText(), user);

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
