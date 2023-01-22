package de.hsos.swa.application.input;

import de.hsos.swa.application.output.Result;
import de.hsos.swa.application.input.dto.in.GetCommentByIdInputPortRequest;
import de.hsos.swa.domain.entity.Comment;

import javax.validation.Valid;

public interface GetCommentByIdInputPort {
    Result<Comment> getCommentById(@Valid GetCommentByIdInputPortRequest request);
}
