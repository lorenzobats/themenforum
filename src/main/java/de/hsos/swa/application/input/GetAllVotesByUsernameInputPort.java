package de.hsos.swa.application.input;

import de.hsos.swa.application.input.dto.in.GetAllVotesByUsernameInputPortRequest;
import de.hsos.swa.application.input.dto.out.VoteInputPortDto;
import de.hsos.swa.application.util.Result;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import java.util.List;

public interface GetAllVotesByUsernameInputPort {
    Result<List<VoteInputPortDto>> getAllVotesByUsername(GetAllVotesByUsernameInputPortRequest request, @Context SecurityContext securityContext);
}
