package de.hsos.swa.application.input.command;

import de.hsos.swa.application.annotations.InputPort;
import de.hsos.swa.application.input.dto.in.DeletePostCommand;
import de.hsos.swa.application.input.dto.out.ApplicationResult;
import de.hsos.swa.domain.entity.Post;

import javax.validation.Valid;
import java.util.Optional;

/**
 * Das Interface DeletePostUseCase definiert den Input Port für InteraktionsAdapter zur Nutzung des
 * DeletePostService im Application-Hexagon.
 *
 * @author Lorenzo Battiston
 * @author Oliver Schlüter
 * @version 1.0
 * @see de.hsos.swa.application.service.command.DeletePostService    Implementierender Service dieses Input Ports
 * @see DeletePostCommand                                            Übergebenes Request-DTO an diesen Input Port
 */
@InputPort
public interface DeletePostUseCase {
    ApplicationResult<Optional<Post>> deletePost(@Valid DeletePostCommand command, String requestingUser);
}
