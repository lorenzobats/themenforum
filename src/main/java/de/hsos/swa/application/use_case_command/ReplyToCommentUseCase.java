package de.hsos.swa.application.use_case_command;

import de.hsos.swa.application.input.ReplyToCommentInputPort;
import de.hsos.swa.application.input.dto.in.ReplyToCommentInputPortRequest;
import de.hsos.swa.application.output.repository.RepositoryResult;
import de.hsos.swa.application.output.repository.UserRepository;
import de.hsos.swa.application.output.repository.PostRepository;
import de.hsos.swa.application.util.Result;
import de.hsos.swa.domain.entity.Comment;
import de.hsos.swa.domain.entity.Post;
import de.hsos.swa.domain.entity.User;
import de.hsos.swa.domain.factory.CommentFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

@RequestScoped
@Transactional(Transactional.TxType.REQUIRES_NEW)
public class ReplyToCommentUseCase implements ReplyToCommentInputPort {
    @Inject
    PostRepository postRepository;

    @Inject
    UserRepository userRepository;


    @Override
    public Result<Comment> replyToComment(ReplyToCommentInputPortRequest request) {
        RepositoryResult<User> getUserResponse = this.userRepository.getUserByName(request.username());
        if (getUserResponse.badResult()) {
            return Result.error("User does not exist");
        }

        User user = getUserResponse.get();

        Result<Post> postResult = this.postRepository.getPostByCommentId(UUID.fromString(request.commentId()));
        if (!postResult.isSuccessful()) {
            return Result.error(postResult.getMessage());
        }

        Post post = postResult.getData();

        Comment reply = CommentFactory.createComment(request.commentText(), user);

        if(!post.addReplyToComment(request.commentId(), reply)){
            return Result.error("Comment is inactive");
        }

        Result<Post> updatedPostResult = this.postRepository.updatePost(post);

        if (updatedPostResult.isSuccessful()) {
            Optional<Comment> savedComment = updatedPostResult.getData().findCommentById(reply.getId().toString());
            return savedComment.map(Result::success).orElseGet(() -> Result.error("Reply not saved"));
        }

        return Result.error("Something went wrong " + updatedPostResult.getMessage());
    }
}
