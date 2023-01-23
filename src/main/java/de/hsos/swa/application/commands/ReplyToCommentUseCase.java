package de.hsos.swa.application.commands;

import de.hsos.swa.application.output.Result;
import de.hsos.swa.application.input.ReplyToCommentInputPort;
import de.hsos.swa.application.input.dto.in.ReplyToCommentInputPortRequest;
import de.hsos.swa.application.output.repository.UserRepository;
import de.hsos.swa.application.output.repository.PostRepository;
import de.hsos.swa.domain.entity.Comment;
import de.hsos.swa.domain.entity.Post;
import de.hsos.swa.domain.entity.User;
import de.hsos.swa.domain.factory.CommentFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class ReplyToCommentUseCase implements ReplyToCommentInputPort {
    @Inject
    PostRepository postRepository;

    @Inject
    UserRepository userRepository;


    @Override
    public Result<Comment> replyToComment(ReplyToCommentInputPortRequest request) {
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

        Comment reply = CommentFactory.createComment(request.getCommentText(), user);

        post.addReplyToComment(request.getCommentId(), reply);

        Result<Post> updatePostResponse = this.postRepository.updatePost(post);

        if (updatePostResponse.isSuccessful()) {
            Optional<Comment> savedComment = updatePostResponse.getData().findCommentById(reply.getId().toString());
            return savedComment.map(Result::success).orElseGet(() -> Result.error("Reply not saved"));
        }

        return Result.error("Something went wrong " + updatePostResponse.getErrorMessage());

    }
}
