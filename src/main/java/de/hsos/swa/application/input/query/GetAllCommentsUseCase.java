package de.hsos.swa.application.input.query;

import de.hsos.swa.application.annotations.InputPort;
import de.hsos.swa.application.input.dto.in.GetAllCommentsQuery;
import de.hsos.swa.application.input.dto.out.ApplicationResult;
import de.hsos.swa.domain.entity.Comment;

import javax.validation.Valid;
import java.util.List;


/**
 * Das Interface GetAllCommentsUseCase definiert den Input Port für InteraktionsAdapter zur Nutzung des
 * GetAllCommentsService im Application-Hexagon.
 *
 * @author Lorenzo Battiston
 * @author Oliver Schlüter
 * @version 1.0
 * @see de.hsos.swa.application.service.query.GetAllCommentsService    Implementierender Service dieses Input Ports
 * @see GetAllCommentsQuery                                            Übergebenes Request-DTO an diesen Input Port
 */
@InputPort
public interface GetAllCommentsUseCase {
    ApplicationResult<List<Comment>> getAllComments(@Valid GetAllCommentsQuery query);
}
