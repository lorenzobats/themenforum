package de.hsos.swa.application.input.command;

import de.hsos.swa.application.annotations.InputPort;
import de.hsos.swa.application.input.dto.in.UpdatePostCommand;
import de.hsos.swa.application.input.dto.out.ApplicationResult;
import de.hsos.swa.domain.entity.Post;

import javax.validation.Valid;
@InputPort
public interface UpdatePostUseCase {
   ApplicationResult<Post> updatePost(@Valid UpdatePostCommand request, String requestingUser);
}
