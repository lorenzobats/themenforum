package de.hsos.swa.application.input.command;

import de.hsos.swa.application.annotations.InputPort;
import de.hsos.swa.application.input.dto.in.DeleteCommentCommand;
import de.hsos.swa.application.input.dto.out.ApplicationResult;
import de.hsos.swa.domain.entity.Comment;

import javax.validation.Valid;
import java.util.Optional;

/**
 * Das Interface DeleteCommentUseCase definiert den Input Port für InteraktionsAdapter zur Nutzung des
 * DeleteCommentService im Application-Hexagon.
 *
 * @author Lorenzo Battiston
 * @author Oliver Schlüter
 * @version 1.0
 * @see de.hsos.swa.application.service.command.DeleteCommentService    Implementierender Service dieses Input Ports
 * @see DeleteCommentCommand                                            Übergebenes Request-DTO an diesen Input Port
 */
@InputPort
public interface DeleteCommentUseCase {
    ApplicationResult<Optional<Comment>> deleteComment(@Valid DeleteCommentCommand command, String requestingUser);
}
