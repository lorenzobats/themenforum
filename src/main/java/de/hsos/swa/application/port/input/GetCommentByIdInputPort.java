package de.hsos.swa.application.port.input;

import de.hsos.swa.application.port.input.request.GetCommentByIdInputPortRequest;
import de.hsos.swa.domain.entity.Comment;

import javax.validation.Valid;

public interface GetCommentByIdInputPort {
    Result<Comment> getCommentById(@Valid GetCommentByIdInputPortRequest request);
}
