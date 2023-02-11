package de.hsos.swa.application.input.command;

import de.hsos.swa.application.annotations.InputPort;
import de.hsos.swa.application.input.dto.out.ApplicationResult;
import de.hsos.swa.application.input.dto.in.CommentPostCommand;
import de.hsos.swa.domain.entity.Comment;

import javax.validation.Valid;

/**
 * Das Interface CommentPostUseCase definiert den Input Port für InteraktionsAdapter zur Nutzung des
 * CommentPostService im Application-Hexagon.
 *
 * @author Lorenzo Battiston
 * @author Oliver Schlüter
 * @version 1.0
 * @see de.hsos.swa.application.service.command.CommentPostService    Implementierender Service dieses Input Ports
 * @see CommentPostCommand                                            Übergebenes Request-DTO an diesen Input Port
 */
@InputPort
public interface CommentPostUseCase {
   ApplicationResult<Comment> commentPost(@Valid CommentPostCommand command, String requestingUser);
}
