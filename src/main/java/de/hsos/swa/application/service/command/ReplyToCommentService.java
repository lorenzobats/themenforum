package de.hsos.swa.application.service.command;

import de.hsos.swa.application.annotations.ApplicationService;
import de.hsos.swa.application.input.ReplyToCommentUseCase;
import de.hsos.swa.application.input.dto.in.ReplyToCommentCommand;
import de.hsos.swa.application.input.dto.out.Result;
import de.hsos.swa.application.output.repository.UserRepository;
import de.hsos.swa.application.output.repository.PostRepository;
import de.hsos.swa.application.output.repository.dto.out.RepositoryResult;
import de.hsos.swa.domain.entity.Comment;
import de.hsos.swa.domain.entity.Post;
import de.hsos.swa.domain.entity.User;
import de.hsos.swa.domain.factory.CommentFactory;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

@RequestScoped
@Transactional(Transactional.TxType.REQUIRES_NEW)
@ApplicationService
public class ReplyToCommentService implements ReplyToCommentUseCase {
    @Inject
    PostRepository postRepository;

    @Inject
    UserRepository userRepository;


    @Override
    public Result<Comment> replyToComment(ReplyToCommentCommand request) {
        de.hsos.swa.application.output.repository.dto.out.RepositoryResult<User> getUserResponse = this.userRepository.getUserByName(request.username());
        if (getUserResponse.badResult()) {
            return Result.error("User does not exist");
        }

        User user = getUserResponse.get();

        RepositoryResult<Post> postResult = this.postRepository.getPostByCommentId(UUID.fromString(request.commentId()));
        if (postResult.badResult()) {
            return Result.error("");
        }

        Post post = postResult.get();

        Comment reply = CommentFactory.createComment(request.commentText(), user);

        if(!post.addReplyToComment(UUID.fromString(request.commentId()), reply)){
            return Result.error("Comment is inactive");
        }

        RepositoryResult<Post> updatedPostResult = this.postRepository.updatePost(post);

        if (updatedPostResult.ok()) {
            return Result.success(reply);
        }

        return Result.error("Something went wrong ");
    }
}
