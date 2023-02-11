package de.hsos.swa.application.input.query;

import de.hsos.swa.application.annotations.InputPort;
import de.hsos.swa.application.input.dto.in.GetAllVotesByUsernameQuery;
import de.hsos.swa.application.input.dto.out.ApplicationResult;
import de.hsos.swa.application.input.dto.out.VoteWithVotedEntityReference;

import javax.validation.Valid;
import java.util.List;

/**
 * Das Interface GetAllVotesByUsernameUseCase definiert den Input Port für InteraktionsAdapter zur Nutzung des
 * GetAllVotesByUsernameService im Application-Hexagon.
 *
 * @author Lorenzo Battiston
 * @author Oliver Schlüter
 * @version 1.0
 * @see de.hsos.swa.application.service.query.GetAllVotesByUsernameService    Implementierender Service dieses Input Ports
 * @see GetAllVotesByUsernameQuery                                            Übergebenes Request-DTO an diesen Input Port
 */
@InputPort
public interface GetAllVotesByUsernameUseCase {
    ApplicationResult<List<VoteWithVotedEntityReference>> getAllVotesByUsername(@Valid GetAllVotesByUsernameQuery query, String requestingUser);
}
