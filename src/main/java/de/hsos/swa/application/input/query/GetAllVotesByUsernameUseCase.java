package de.hsos.swa.application.input.query;

import de.hsos.swa.application.annotations.InputPort;
import de.hsos.swa.application.input.dto.in.GetAllVotesByUsernameQuery;
import de.hsos.swa.application.input.dto.out.ApplicationResult;
import de.hsos.swa.application.input.dto.out.VoteWithVotedEntityReference;

import javax.validation.Valid;
import java.util.List;

@InputPort
public interface GetAllVotesByUsernameUseCase {
    ApplicationResult<List<VoteWithVotedEntityReference>> getAllVotesByUsername(@Valid GetAllVotesByUsernameQuery query, String requestingUser);
}
