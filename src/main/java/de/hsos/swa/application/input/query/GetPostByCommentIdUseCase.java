package de.hsos.swa.application.input.query;

import de.hsos.swa.application.annotations.InputPort;
import de.hsos.swa.application.input.dto.in.GetPostByCommentIdQuery;
import de.hsos.swa.application.input.dto.out.ApplicationResult;
import de.hsos.swa.domain.entity.Post;

import javax.validation.Valid;

/**
 * Das Interface GetPostByCommentIdUseCase definiert den Input Port für InteraktionsAdapter zur Nutzung des
 * GetPostByCommentIdService im Application-Hexagon.
 *
 * @author Lorenzo Battiston
 * @author Oliver Schlüter
 * @version 1.0
 * @see de.hsos.swa.application.service.query.GetPostByCommentIdService    Implementierender Service dieses Input Ports
 * @see GetPostByCommentIdQuery                                            Übergebenes Request-DTO an diesen Input Port
 */
@InputPort
public interface GetPostByCommentIdUseCase {
    ApplicationResult<Post> getPostByCommentId(@Valid GetPostByCommentIdQuery query);
}
