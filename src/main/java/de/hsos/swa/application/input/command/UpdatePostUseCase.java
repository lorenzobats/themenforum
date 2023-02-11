package de.hsos.swa.application.input.command;

import de.hsos.swa.application.annotations.InputPort;
import de.hsos.swa.application.input.dto.in.UpdatePostCommand;
import de.hsos.swa.application.input.dto.out.ApplicationResult;
import de.hsos.swa.domain.entity.Post;

import javax.validation.Valid;

/**
 * Das Interface UpdatePostUseCase definiert den Input Port für InteraktionsAdapter zur Nutzung des
 * UpdatePostService im Application-Hexagon.
 *
 * @author Lorenzo Battiston
 * @author Oliver Schlüter
 * @version 1.0
 * @see de.hsos.swa.application.service.command.UpdatePostService    Implementierender Service dieses Input Ports
 * @see UpdatePostCommand                                            Übergebenes Request-DTO an diesen Input Port
 */
@InputPort
public interface UpdatePostUseCase {
   ApplicationResult<Post> updatePost(@Valid UpdatePostCommand request, String requestingUser);
}
