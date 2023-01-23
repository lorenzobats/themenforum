package de.hsos.swa.application;

import de.hsos.swa.application.input.VotePostInputPort;
import de.hsos.swa.application.input.dto.in.VotePostInputPortRequest;
import de.hsos.swa.application.output.repository.PostRepository;
import de.hsos.swa.application.output.repository.UserRepository;
import de.hsos.swa.application.util.Result;
import de.hsos.swa.domain.entity.Post;
import de.hsos.swa.domain.entity.User;
import de.hsos.swa.domain.service.VoteService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.UUID;

@ApplicationScoped
public class VotePostUseCase implements VotePostInputPort {

    @Inject
    PostRepository postRepository;

    @Inject
    UserRepository userRepository;

    @Inject
    VoteService voteService;



    @Override
    public Result<Post> votePost(VotePostInputPortRequest request) {
        Result<User> userResult = this.userRepository.getUserByName(request.getUsername());
        if (!userResult.isSuccessful()) {
            return Result.error("User does not exist");
        }
        User user = userResult.getData();

        Result<Post> postResult = this.postRepository.getPostById(UUID.fromString(request.getPostId()), true);
        if (!postResult.isSuccessful()) {
            return Result.error("Post does not exist");
        }
        Post post = postResult.getData();

        try {
            this.voteService.votePost(post, user, request.getVoteType());
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }

        Result<Post> updatePostResult = this.postRepository.updatePost(post);

        if (updatePostResult.isSuccessful()) {
            return Result.success(updatePostResult.getData());
        }

        return Result.error("Something went wrong " + updatePostResult.getErrorMessage());
    }

}
