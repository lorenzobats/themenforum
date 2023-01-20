package de.hsos.swa.application.port.input;

import de.hsos.swa.application.port.input.request.GetPostByIdInputPortRequest;
import de.hsos.swa.domain.entity.Post;

import javax.validation.Valid;

public interface GetPostByIdInputPort {
    Result<Post> getPostById(@Valid GetPostByIdInputPortRequest request);
}
