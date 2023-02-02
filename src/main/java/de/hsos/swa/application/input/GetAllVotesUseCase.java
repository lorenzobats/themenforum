package de.hsos.swa.application.input;

import de.hsos.swa.application.annotations.InputPort;
import de.hsos.swa.application.input.dto.out.VoteWithVotedEntityReferenceDto;
import de.hsos.swa.application.input.dto.out.ApplicationResult;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import java.util.List;
@InputPort
public interface GetAllVotesUseCase {
    ApplicationResult<List<VoteWithVotedEntityReferenceDto>> getAllVotes(@Context SecurityContext securityContext);
}
