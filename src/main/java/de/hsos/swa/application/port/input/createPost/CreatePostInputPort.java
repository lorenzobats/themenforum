package de.hsos.swa.application.port.input.createPost;

import de.hsos.swa.application.port.input._shared.Result;

import javax.validation.Valid;

public interface CreatePostInputPort {
   Result<CreatePostInputPortResponse> createPost(@Valid CreatePostInputPortRequest inputPortRequest);
}
