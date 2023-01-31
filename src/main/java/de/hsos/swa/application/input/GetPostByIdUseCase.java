package de.hsos.swa.application.input;

import de.hsos.swa.application.annotations.InputPort;
import de.hsos.swa.application.input.dto.out.Result;
import de.hsos.swa.application.input.dto.in.GetPostByIdQuery;
import de.hsos.swa.domain.entity.Post;

import javax.validation.Valid;
@InputPort
public interface GetPostByIdUseCase {
    Result<Post> getPostById(@Valid GetPostByIdQuery request);
}
