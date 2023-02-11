package de.hsos.swa.application.input.query;

import de.hsos.swa.application.annotations.InputPort;
import de.hsos.swa.application.input.dto.out.VoteWithVotedEntityReference;
import de.hsos.swa.application.input.dto.out.ApplicationResult;

import java.util.List;

/**
 * Das Interface GetAllVotesUseCase definiert den Input Port für InteraktionsAdapter zur Nutzung des
 * GetAllVotesService im Application-Hexagon.
 *
 * @author Lorenzo Battiston
 * @author Oliver Schlüter
 * @version 1.0
 * @see de.hsos.swa.application.service.query.GetAllVotesService    Implementierender Service dieses Input Ports
 */
@InputPort
public interface GetAllVotesUseCase {
    ApplicationResult<List<VoteWithVotedEntityReference>> getAllVotes(String requestingUser);
}
