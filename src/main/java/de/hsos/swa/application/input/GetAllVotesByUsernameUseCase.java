package de.hsos.swa.application.input;

import de.hsos.swa.application.annotations.InputPort;
import de.hsos.swa.application.input.dto.in.GetAllVotesByUsernameQuery;
import de.hsos.swa.application.input.dto.out.Result;
import de.hsos.swa.application.input.dto.out.VoteInputPortDto;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import java.util.List;
@InputPort
public interface GetAllVotesByUsernameUseCase {
    Result<List<VoteInputPortDto>> getAllVotesByUsername(GetAllVotesByUsernameQuery request, @Context SecurityContext securityContext);
}
