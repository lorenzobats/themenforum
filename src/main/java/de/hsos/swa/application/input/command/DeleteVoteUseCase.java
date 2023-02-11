package de.hsos.swa.application.input.command;

import de.hsos.swa.application.annotations.InputPort;
import de.hsos.swa.application.input.dto.in.DeleteVoteCommand;
import de.hsos.swa.application.input.dto.out.ApplicationResult;
import de.hsos.swa.domain.entity.Vote;

import javax.validation.Valid;
import java.util.Optional;

/**
 * Das Interface DeleteVoteUseCase definiert den Input Port für InteraktionsAdapter zur Nutzung des
 * DeleteVoteService im Application-Hexagon.
 *
 * @author Lorenzo Battiston
 * @author Oliver Schlüter
 * @version 1.0
 * @see de.hsos.swa.application.service.command.DeleteVoteService    Implementierender Service dieses Input Ports
 * @see DeleteVoteCommand                                            Übergebenes Request-DTO an diesen Input Port
 */
@InputPort
public interface DeleteVoteUseCase {
    ApplicationResult<Optional<Vote>> deleteVote(@Valid DeleteVoteCommand command, String requestingUser);
}
