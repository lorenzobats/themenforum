package de.hsos.swa.application.input;

import de.hsos.swa.application.annotations.InputPort;
import de.hsos.swa.application.input.dto.out.Result;
import de.hsos.swa.application.input.dto.in.CreatePostCommand;
import de.hsos.swa.domain.entity.Post;

import javax.validation.Valid;
@InputPort
public interface CreatePostUseCase {
   Result<Post> createPost(@Valid CreatePostCommand request);
}
