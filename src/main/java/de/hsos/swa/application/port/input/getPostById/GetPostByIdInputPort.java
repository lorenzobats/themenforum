package de.hsos.swa.application.port.input.getPostById;

import de.hsos.swa.application.port.input._shared.Result;

public interface GetPostByIdInputPort {
    Result<GetPostByIdInputPortResponse> getPostById(GetPostByIdInputPortRequest request);
}
