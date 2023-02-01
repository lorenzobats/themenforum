package de.hsos.swa.application.input;

import de.hsos.swa.application.annotations.InputPort;
import de.hsos.swa.application.input.dto.in.UpdatePostCommand;
import de.hsos.swa.application.input.dto.out.Result;
import de.hsos.swa.domain.entity.Post;

import javax.validation.Valid;
@InputPort
public interface UpdatePostUseCase {
   Result<Post> updatePost(@Valid UpdatePostCommand request);
}