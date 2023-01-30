package de.hsos.swa.application.use_case_command;

import de.hsos.swa.application.input.DeleteVoteInputPort;
import de.hsos.swa.application.input.dto.in.DeleteVoteInputPortRequest;
import de.hsos.swa.application.output.dto.VotePersistenceDto;
import de.hsos.swa.application.output.repository.PostRepository;
import de.hsos.swa.application.output.repository.UserRepository;
import de.hsos.swa.application.output.repository.VoteRepository;
import de.hsos.swa.application.util.Result;
import de.hsos.swa.domain.entity.*;
import de.hsos.swa.domain.service.VoteEntityService;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

@RequestScoped
@Transactional(Transactional.TxType.REQUIRES_NEW)
public class DeleteVoteUseCase implements DeleteVoteInputPort {

    @Inject
    VoteRepository voteRepository;

    @Inject
    PostRepository postRepository;

    @Inject
    UserRepository userRepository;

    @Inject
    VoteEntityService voteEntityService;

    @Override
    public Result<Vote> deleteVote(DeleteVoteInputPortRequest request) {
        Result<VotePersistenceDto> voteResult = this.voteRepository.getVoteById(UUID.fromString(request.vote()));
        if (!voteResult.isSuccessful()) {
            return Result.error("Cannot find Vote");
        }

        VotePersistenceDto vote = voteResult.getData();

        Result<Post> postResult = new Result<>();
        Optional<Vote> optionalVote = Optional.empty();
        switch (vote.votedEntityType()) {
            case COMMENT -> {
                postResult = this.postRepository.getPostById(vote.votedEntityId(), false);
                if (postResult.isSuccessful()) {
                    Optional<Comment> optionalComment = postResult.getData().findCommentById(String.valueOf(vote.votedEntityId()));
                    if (optionalComment.isPresent()) {
                        optionalVote = this.voteEntityService.deleteVote(optionalComment.get(), vote.vote().getUser());
                    }
                }
            }
            case POST -> {
                postResult = this.postRepository.getPostByCommentId(vote.votedEntityId());
                optionalVote = this.voteEntityService.deleteVote(postResult.getData(), vote.vote().getUser());
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
