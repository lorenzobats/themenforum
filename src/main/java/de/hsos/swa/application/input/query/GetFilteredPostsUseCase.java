package de.hsos.swa.application.input.query;

import de.hsos.swa.application.annotations.InputPort;
import de.hsos.swa.application.input.dto.in.GetFilteredPostsQuery;
import de.hsos.swa.application.input.dto.out.ApplicationResult;
import de.hsos.swa.domain.entity.Post;

import javax.validation.Valid;
import java.util.List;

/**
 * Das Interface GetFilteredPostsUseCase definiert den Input Port für InteraktionsAdapter zur Nutzung des
 * GetFilteredPostsService im Application-Hexagon.
 *
 * @author Lorenzo Battiston
 * @author Oliver Schlüter
 * @version 1.0
 * @see de.hsos.swa.application.service.query.GetFilteredPostsService    Implementierender Service dieses Input Ports
 * @see GetFilteredPostsQuery                                            Übergebenes Request-DTO an diesen Input Port
 */
@InputPort
public interface GetFilteredPostsUseCase {
    ApplicationResult<List<Post>> getFilteredPosts(@Valid GetFilteredPostsQuery query);
}
