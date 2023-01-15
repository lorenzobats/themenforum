package de.hsos.swa.application.port.input.createPost;

import de.hsos.swa.application.port.input._shared.Result;

public interface CreatePostInputPort {
   Result<CreatePostInputPortResponse> createPost(CreatePostInputPortRequest inputPortRequest);
}
