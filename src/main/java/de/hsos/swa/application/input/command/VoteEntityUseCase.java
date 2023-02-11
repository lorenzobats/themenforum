package de.hsos.swa.application.input.command;

import de.hsos.swa.application.annotations.InputPort;
import de.hsos.swa.application.input.dto.out.ApplicationResult;
import de.hsos.swa.application.input.dto.in.VoteEntityCommand;
import de.hsos.swa.domain.entity.Vote;

import javax.validation.Valid;

/**
 * Das Interface VoteEntityUseCase definiert den Input Port für InteraktionsAdapter zur Nutzung des
 * VoteEntityService im Application-Hexagon.
 *
 * @author Lorenzo Battiston
 * @author Oliver Schlüter
 * @version 1.0
 * @see de.hsos.swa.application.service.command.VoteEntityService    Implementierender Service dieses Input Ports
 * @see VoteEntityCommand                                            Übergebenes Request-DTO an diesen Input Port
 */
@InputPort
public interface VoteEntityUseCase {
   ApplicationResult<Vote> vote(@Valid VoteEntityCommand command, String requestingUser);
}
