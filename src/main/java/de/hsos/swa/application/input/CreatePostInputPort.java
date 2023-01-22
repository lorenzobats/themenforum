package de.hsos.swa.application.input;

import de.hsos.swa.application.output.Result;
import de.hsos.swa.application.input.dto.in.CreatePostInputPortRequest;
import de.hsos.swa.domain.entity.Post;

import javax.validation.Valid;

public interface CreatePostInputPort {
   Result<Post> createPost(@Valid CreatePostInputPortRequest request);
}
