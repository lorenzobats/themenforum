package de.hsos.swa.application.service.command;

import de.hsos.swa.application.annotations.ApplicationService;
import de.hsos.swa.application.input.DeleteVoteUseCase;
import de.hsos.swa.application.input.dto.in.DeleteVoteCommand;
import de.hsos.swa.application.output.repository.dto.in.VoteQueryDto;
import de.hsos.swa.application.output.repository.PostRepository;
import de.hsos.swa.application.output.repository.dto.out.RepositoryResult;
import de.hsos.swa.application.output.repository.UserRepository;
import de.hsos.swa.application.output.repository.VoteRepository;
import de.hsos.swa.application.input.dto.out.Result;
import de.hsos.swa.domain.entity.*;
import de.hsos.swa.domain.service.VoteEntityService;
import org.jboss.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

@RequestScoped
@Transactional(Transactional.TxType.REQUIRES_NEW)
@ApplicationService
public class DeleteVoteService implements DeleteVoteUseCase {

    @Inject
    VoteRepository voteRepository;

    @Inject
    PostRepository postRepository;

    @Inject
    VoteEntityService voteEntityService;

    @Inject
    UserRepository userRepository;

    @Inject
    Logger log;

    @Override
    public Result<Vote> deleteVote(DeleteVoteCommand request) {
        RepositoryResult<User> userResult = this.userRepository.getUserByName(request.username());
        if (userResult.badResult()) {
            return Result.error("Cannot retrieve User");
        }
        User user = userResult.get();


        RepositoryResult<VoteQueryDto> voteResult = this.voteRepository.getVoteById(UUID.fromString(request.vote()));
        if (voteResult.badResult()) {
            return Result.error("Cannot find Vote");
        }
        VoteQueryDto vote = voteResult.get();

        Result<Post> postResult = new Result<>();
        Optional<Vote> optionalVote = Optional.empty();
        switch (vote.votedEntityType()) {
            case COMMENT -> {
                postResult = this.postRepository.getPostByCommentId(vote.votedEntityId());
                if (postResult.isSuccessful()) {
                    Optional<Comment> optionalComment = postResult.getData().findCommentById(String.valueOf(vote.votedEntityId()));
                    if (optionalComment.isPresent()) {
                        optionalVote = this.voteEntityService.deleteVote(optionalComment.get(), user);
                    }
                }
            }
            case POST -> {
                postResult = this.postRepository.getPostById(vote.votedEntityId(),true);
                if (postResult.isSuccessful()) {
                    optionalVote = this.voteEntityService.deleteVote(postResult.getData(), user);
                }
            }
        }

        if (optionalVote.isEmpty()) {
            return Result.error("Vote could not be deleted");
        }

        Result<Post> updatePostResult = this.postRepository.updatePost(postResult.getData());

        if (updatePostResult.isSuccessful()) {
            return Result.success(optionalVote.get());
        }

        return Result.error("Something went wrong while deleting a Vote and updating the Post" + updatePostResult.getMessage());
    }
}
