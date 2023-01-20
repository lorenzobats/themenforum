package de.hsos.swa.application.input;

import de.hsos.swa.application.Result;
import de.hsos.swa.application.input.request.GetPostByIdInputPortRequest;
import de.hsos.swa.domain.entity.Post;

import javax.validation.Valid;

public interface GetPostByIdInputPort {
    Result<Post> getPostById(@Valid GetPostByIdInputPortRequest request);
}
