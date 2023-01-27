package de.hsos.swa.application.use_case_command;

import de.hsos.swa.application.input.VoteCommentInputPort;
import de.hsos.swa.application.input.dto.in.VoteCommentInputPortRequest;
import de.hsos.swa.application.output.repository.PostRepository;
import de.hsos.swa.application.output.repository.UserRepository;
import de.hsos.swa.application.util.Result;
import de.hsos.swa.domain.entity.Comment;
import de.hsos.swa.domain.entity.Post;
import de.hsos.swa.domain.entity.User;
import de.hsos.swa.domain.service.VoteService;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

@RequestScoped
@Transactional(Transactional.TxType.REQUIRES_NEW)
public class VoteCommentUseCase implements VoteCommentInputPort {

    @Inject
    PostRepository postRepository;

    @Inject
    UserRepository userRepository;

    @Inject
    VoteService voteService;


    @Override
    public Result<Comment> voteComment(VoteCommentInputPortRequest request) {
        Result<User> userResult = this.userRepository.getUserByName(request.username());
        if (!userResult.isSuccessful()) {
            return Result.error("Cannot retrieve User");
        }
        User user = userResult.getData();


        Result<Post> postResult = this.postRepository.getPostByCommentId(UUID.fromString(request.commentId()));
        if (!postResult.isSuccessful()) {
            return Result.error("Cannot retrieve Post");
        }
        Post post = postResult.getData();

        try {
            this.voteService.voteComment(post, user, request.commentId(), request.voteType());
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }

        Result<Post> updatePostResult = this.postRepository.updatePost(post);
        if (!updatePostResult.isSuccessful()) {
            return Result.error("Updating Comment went wrong " + updatePostResult.getMessage());
        }

        Optional<Comment> optionalComment = post.findCommentById(request.commentId());
        if(optionalComment.isEmpty()){
            return Result.error("Updating Comment went wrong " + updatePostResult.getMessage());
        }
        return Result.success(optionalComment.get());
    }
}
