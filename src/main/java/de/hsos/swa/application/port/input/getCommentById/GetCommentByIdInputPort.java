package de.hsos.swa.application.port.input.getCommentById;

import de.hsos.swa.application.port.input._shared.Result;

import javax.validation.Valid;

public interface GetCommentByIdInputPort {
    Result<GetCommentByIdInputPortResponse> getCommentById(@Valid GetCommentByIdInputPortRequest request);
}
