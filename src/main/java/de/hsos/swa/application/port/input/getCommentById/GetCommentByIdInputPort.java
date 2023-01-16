package de.hsos.swa.application.port.input.getCommentById;

import de.hsos.swa.application.port.input._shared.Result;

public interface GetCommentByIdInputPort {
    Result<GetCommentByIdInputPortResponse> getCommentById(GetCommentByIdInputPortRequest request);
}
