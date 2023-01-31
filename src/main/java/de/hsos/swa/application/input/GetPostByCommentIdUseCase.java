package de.hsos.swa.application.input;

import de.hsos.swa.application.annotations.InputPort;
import de.hsos.swa.application.input.dto.in.GetPostByCommentIdQuery;
import de.hsos.swa.application.input.dto.out.Result;
import de.hsos.swa.domain.entity.Post;

import javax.validation.Valid;
@InputPort
public interface GetPostByCommentIdUseCase {
    Result<Post> getPostByCommentId(@Valid GetPostByCommentIdQuery request);
}
