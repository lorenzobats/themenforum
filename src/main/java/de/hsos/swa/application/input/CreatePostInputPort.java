package de.hsos.swa.application.input;

import de.hsos.swa.application.Result;
import de.hsos.swa.application.input.request.CreatePostInputPortRequest;
import de.hsos.swa.domain.entity.Post;

import javax.validation.Valid;

public interface CreatePostInputPort {
   Result<Post> createPost(@Valid CreatePostInputPortRequest request);
}
