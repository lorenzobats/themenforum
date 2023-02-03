package de.hsos.swa.application.input;

import de.hsos.swa.application.annotations.InputPort;
import de.hsos.swa.application.input.dto.out.ApplicationResult;
import de.hsos.swa.application.input.dto.in.CreatePostCommand;
import de.hsos.swa.domain.entity.Post;

import javax.validation.Valid;

@InputPort
public interface CreatePostUseCase {
   ApplicationResult<Post> createPost(@Valid CreatePostCommand command, String requestingUser);
}
