package de.hsos.swa.application.input.query;

import de.hsos.swa.application.annotations.InputPort;
import de.hsos.swa.application.input.dto.out.ApplicationResult;
import de.hsos.swa.application.input.dto.in.GetPostByIdQuery;
import de.hsos.swa.domain.entity.Post;

import javax.validation.Valid;

/**
 * Das Interface GetPostByIdUseCase definiert den Input Port für InteraktionsAdapter zur Nutzung des
 * GetPostByIdService im Application-Hexagon.
 *
 * @author Lorenzo Battiston
 * @author Oliver Schlüter
 * @version 1.0
 * @see de.hsos.swa.application.service.query.GetPostByIdService    Implementierender Service dieses Input Ports
 * @see GetPostByIdQuery                                            Übergebenes Request-DTO an diesen Input Port
 */
@InputPort
public interface GetPostByIdUseCase {
    ApplicationResult<Post> getPostById(@Valid GetPostByIdQuery query);
}
