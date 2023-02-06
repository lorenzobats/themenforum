package de.hsos.swa.application.input.command;

import de.hsos.swa.application.annotations.InputPort;
import de.hsos.swa.application.input.dto.in.DeleteVoteCommand;
import de.hsos.swa.application.input.dto.out.ApplicationResult;
import de.hsos.swa.domain.entity.Vote;

import javax.validation.Valid;
import java.util.Optional;

@InputPort
public interface DeleteVoteUseCase {
    ApplicationResult<Optional<Vote>> deleteVote(@Valid DeleteVoteCommand command, String requestingUser);
}
