package de.hsos.swa.application.input.query;

import de.hsos.swa.application.annotations.InputPort;
import de.hsos.swa.application.input.dto.in.GetPostByCommentIdQuery;
import de.hsos.swa.application.input.dto.out.ApplicationResult;
import de.hsos.swa.domain.entity.Post;

import javax.validation.Valid;
@InputPort
public interface GetPostByCommentIdUseCase {
    ApplicationResult<Post> getPostByCommentId(@Valid GetPostByCommentIdQuery query);
}
