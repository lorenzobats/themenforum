package de.hsos.swa.application.use_case_command;

import de.hsos.swa.application.input.VoteEntityInputPort;
import de.hsos.swa.application.input.dto.in.VoteEntityInputPortRequest;
import de.hsos.swa.application.output.repository.PostRepository;
import de.hsos.swa.application.output.repository.RepositoryResult;
import de.hsos.swa.application.output.repository.UserRepository;
import de.hsos.swa.application.util.Result;
import de.hsos.swa.domain.entity.Comment;
import de.hsos.swa.domain.entity.Post;
import de.hsos.swa.domain.entity.User;
import de.hsos.swa.domain.entity.Vote;
import de.hsos.swa.domain.service.VoteEntityService;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

@RequestScoped
@Transactional(Transactional.TxType.REQUIRES_NEW)
public class VoteEntityUseCase implements VoteEntityInputPort {

    @Inject
    PostRepository postRepository;

    @Inject
    UserRepository userRepository;

    @Inject
    VoteEntityService voteEntityService;


    @Override
    public Result<Vote> vote(VoteEntityInputPortRequest request) {
        RepositoryResult<User> userResult = this.userRepository.getUserByName(request.username());
        if (userResult.badResult()) {
            return Result.error("Cannot retrieve User");
        }
        User user = userResult.get();


        Result<Post> postResult = new Result<>();
        Optional<Vote> optionalVote = Optional.empty();
        switch (request.entityType()) {
            case COMMENT -> {
                postResult = this.postRepository.getPostByCommentId(UUID.fromString(request.entityId()));
                if (postResult.isSuccessful()) {
                    Optional<Comment> optionalComment = postResult.getData().findCommentById(request.entityId());
                    if (optionalComment.isPresent()) {
                        optionalVote = this.voteEntityService.vote(optionalComment.get(), user, request.voteType());
                        if (optionalVote.isEmpty()) {
                            return Result.error("Comment could not be voted");
                        }
                    }
                }
            }
            case POST -> {
                postResult = this.postRepository.getPostById(UUID.fromString(request.entityId()),true);
                optionalVote = this.voteEntityService.vote(postResult.getData(), user, request.voteType());
                if (optionalVote.isEmpty()) {
                    return Result.error("Post could not be voted");
                }
            }
        }


        Result<Post> updatePostResult = this.postRepository.updatePost(postResult.getData());

        if (updatePostResult.isSuccessful() && optionalVote.isPresent()) {
            return Result.success(optionalVote.get());
        }

        return Result.error("Something went wrong while voting Entity" + updatePostResult.getMessage());
    }
}
