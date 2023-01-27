package de.hsos.swa.application.input;

import de.hsos.swa.application.input.dto.in.GetPostByCommentIdInputPortRequest;
import de.hsos.swa.application.input.dto.in.GetPostByIdInputPortRequest;
import de.hsos.swa.application.util.Result;
import de.hsos.swa.domain.entity.Post;

import javax.validation.Valid;

public interface GetPostByCommentIdInputPort {
    Result<Post> getPostByCommentId(@Valid GetPostByCommentIdInputPortRequest request);
}
