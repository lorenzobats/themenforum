package de.hsos.swa.application.input;

import de.hsos.swa.application.annotations.InputPort;
import de.hsos.swa.application.input.dto.in.DeleteVoteCommand;
import de.hsos.swa.application.input.dto.out.Result;
import de.hsos.swa.domain.entity.Vote;

import javax.validation.Valid;
@InputPort
public interface DeleteVoteUseCase {
    Result<Vote> deleteVote(@Valid DeleteVoteCommand request);
}
