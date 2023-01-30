package de.hsos.swa.application.input;

import de.hsos.swa.application.input.dto.in.DeleteVoteInputPortRequest;
import de.hsos.swa.application.util.Result;
import de.hsos.swa.domain.entity.Vote;

import javax.validation.Valid;

public interface DeleteVoteInputPort {
    Result<Vote> deleteVote(@Valid DeleteVoteInputPortRequest request);
}
