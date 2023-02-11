package de.hsos.swa.application.input.query;

import de.hsos.swa.application.annotations.InputPort;
import de.hsos.swa.application.input.dto.in.GetCommentsByUserQuery;
import de.hsos.swa.application.input.dto.out.ApplicationResult;
import de.hsos.swa.domain.entity.Comment;

import javax.validation.Valid;
import java.util.List;

/**
 * Das Interface GetCommentsByUserUseCase definiert den Input Port für InteraktionsAdapter zur Nutzung des
 * GetCommentsByUserService im Application-Hexagon.
 *
 * @author Lorenzo Battiston
 * @author Oliver Schlüter
 * @version 1.0
 * @see de.hsos.swa.application.service.query.GetCommentsByUserService    Implementierender Service dieses Input Ports
 * @see GetCommentsByUserQuery                                            Übergebenes Request-DTO an diesen Input Port
 */
@InputPort
public interface GetCommentsByUserUseCase {
    ApplicationResult<List<Comment>> getCommentsByUser(@Valid GetCommentsByUserQuery query);
}