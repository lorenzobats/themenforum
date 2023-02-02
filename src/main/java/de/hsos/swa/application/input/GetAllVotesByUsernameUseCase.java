package de.hsos.swa.application.input;

import de.hsos.swa.application.annotations.InputPort;
import de.hsos.swa.application.input.dto.in.GetAllVotesByUsernameQuery;
import de.hsos.swa.application.input.dto.out.ApplicationResult;
import de.hsos.swa.application.input.dto.out.VoteWithVotedEntityReferenceDto;

import javax.ws.rs.core.Context;
import java.util.List;
@InputPort
public interface GetAllVotesByUsernameUseCase {
    ApplicationResult<List<VoteWithVotedEntityReferenceDto>> getAllVotesByUsername(GetAllVotesByUsernameQuery request, String username);
}
