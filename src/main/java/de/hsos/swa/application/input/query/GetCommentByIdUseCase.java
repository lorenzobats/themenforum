package de.hsos.swa.application.input.query;

import de.hsos.swa.application.annotations.InputPort;
import de.hsos.swa.application.input.dto.out.ApplicationResult;
import de.hsos.swa.application.input.dto.in.GetCommentByIdQuery;
import de.hsos.swa.domain.entity.Comment;

import javax.validation.Valid;

/**
 * Das Interface GetCommentByIdUseCase definiert den Input Port für InteraktionsAdapter zur Nutzung des
 * GetCommentByIdService im Application-Hexagon.
 *
 * @author Lorenzo Battiston
 * @author Oliver Schlüter
 * @version 1.0
 * @see de.hsos.swa.application.service.query.GetCommentByIdService    Implementierender Service dieses Input Ports
 * @see GetCommentByIdQuery                                            Übergebenes Request-DTO an diesen Input Port
 */
@InputPort
public interface GetCommentByIdUseCase {
    ApplicationResult<Comment> getCommentById(@Valid GetCommentByIdQuery query);
}
