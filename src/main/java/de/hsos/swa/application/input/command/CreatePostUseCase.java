package de.hsos.swa.application.input.command;

import de.hsos.swa.application.annotations.InputPort;
import de.hsos.swa.application.input.dto.out.ApplicationResult;
import de.hsos.swa.application.input.dto.in.CreatePostCommand;
import de.hsos.swa.domain.entity.Post;

import javax.validation.Valid;

/**
 * Das Interface CreatePostUseCase definiert den Input Port für InteraktionsAdapter zur Nutzung des
 * CreatePostService im Application-Hexagon.
 *
 * @author Lorenzo Battiston
 * @author Oliver Schlüter
 * @version 1.0
 * @see de.hsos.swa.application.service.command.CreatePostService    Implementierender Service dieses Input Ports
 * @see CreatePostCommand                                            Übergebenes Request-DTO an diesen Input Port
 */
@InputPort
public interface CreatePostUseCase {
   ApplicationResult<Post> createPost(@Valid CreatePostCommand command, String requestingUser);
}
