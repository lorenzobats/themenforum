package de.hsos.swa.application.input;

import de.hsos.swa.application.annotations.InputPort;
import de.hsos.swa.application.input.dto.out.VoteWithVotedEntityReference;
import de.hsos.swa.application.input.dto.out.ApplicationResult;

import javax.ws.rs.core.Context;
import java.util.List;
@InputPort
public interface GetAllVotesUseCase {
    ApplicationResult<List<VoteWithVotedEntityReference>> getAllVotes(String requestingUser);
}
