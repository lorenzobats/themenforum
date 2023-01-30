package de.hsos.swa.application.use_case_command;

import de.hsos.swa.application.input.DeleteVoteInputPort;
import de.hsos.swa.application.input.dto.in.DeleteVoteInputPortRequest;
import de.hsos.swa.application.output.repository.VoteRepository;
import de.hsos.swa.application.util.Result;
import de.hsos.swa.domain.entity.Vote;

import javax.inject.Inject;

public class DeleteVoteUseCase implements DeleteVoteInputPort {

    @Inject
    VoteRepository voteRepository;

    @Override
    public Result<Vote> deleteVote(DeleteVoteInputPortRequest request) {
        return null;
    }
}
