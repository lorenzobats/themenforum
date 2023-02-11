package de.hsos.swa.application.input.command;

import de.hsos.swa.application.annotations.InputPort;
import de.hsos.swa.application.input.dto.out.ApplicationResult;
import de.hsos.swa.application.input.dto.in.ReplyToCommentCommand;
import de.hsos.swa.domain.entity.Comment;

import javax.validation.Valid;

/**
 * Das Interface ReplyToCommentUseCase definiert den Input Port für InteraktionsAdapter zur Nutzung des
 * ReplyToCommentService im Application-Hexagon.
 *
 * @author Lorenzo Battiston
 * @author Oliver Schlüter
 * @version 1.0
 * @see de.hsos.swa.application.service.command.ReplyToCommentService    Implementierender Service dieses Input Ports
 * @see ReplyToCommentCommand                                            Übergebenes Request-DTO an diesen Input Port
 */
@InputPort
public interface ReplyToCommentUseCase {
    ApplicationResult<Comment> replyToComment(@Valid ReplyToCommentCommand command, String requestingUser);
}
