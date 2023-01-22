package de.hsos.swa.application.input;

import de.hsos.swa.application.output.Result;
import de.hsos.swa.application.input.dto.in.GetPostByIdInputPortRequest;
import de.hsos.swa.domain.entity.Post;

import javax.validation.Valid;

public interface GetPostByIdInputPort {
    Result<Post> getPostById(@Valid GetPostByIdInputPortRequest request);
}
