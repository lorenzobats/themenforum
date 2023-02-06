package de.hsos.swa.application.input.query;

import de.hsos.swa.application.annotations.InputPort;
import de.hsos.swa.application.input.dto.out.VoteWithVotedEntityReference;
import de.hsos.swa.application.input.dto.out.ApplicationResult;

import java.util.List;
@InputPort
public interface GetAllVotesUseCase {
    ApplicationResult<List<VoteWithVotedEntityReference>> getAllVotes(String requestingUser);
}
