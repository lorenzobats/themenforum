package de.hsos.swa.application.use_case_command;

import de.hsos.swa.application.input.DeleteVoteInputPort;
import de.hsos.swa.application.input.dto.in.DeleteVoteInputPortRequest;
import de.hsos.swa.application.input.dto.in.VoteCommentInputPortRequest;
import de.hsos.swa.application.input.dto.in.VotePostInputPortRequest;
import de.hsos.swa.application.output.dto.VotePersistenceDto;
import de.hsos.swa.application.output.repository.VoteRepository;
import de.hsos.swa.application.util.Result;
import de.hsos.swa.domain.entity.*;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.UUID;

@RequestScoped
@Transactional(Transactional.TxType.REQUIRES_NEW)
public class DeleteVoteUseCase implements DeleteVoteInputPort {

    @Inject
    VoteRepository voteRepository;

    @Inject
    VoteCommentUseCase voteCommentUseCase;

    @Inject
    VotePostUseCase votePostUseCase;

    @Override
    public Result<Vote> deleteVote(DeleteVoteInputPortRequest request) {

        Result<VotePersistenceDto> voteResult = this.voteRepository.getVoteById(UUID.fromString(request.vote()));
        if (!voteResult.isSuccessful()) {
            return Result.error("Cannot find Vote");
        }
        VotePersistenceDto vote = voteResult.getData();


        try {
            switch (vote.votedEntityType()){
                case COMMENT -> {
                    this.voteCommentUseCase.voteComment(new VoteCommentInputPortRequest(String.valueOf(vote.votedEntityId()), request.username(), VoteType.NONE));
                }
                case POST -> {
                    this.votePostUseCase.votePost(new VotePostInputPortRequest(String.valueOf(vote.votedEntityId()), request.username(), VoteType.NONE));
                }
            }
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }

        return Result.success(vote.vote());
    }
}
