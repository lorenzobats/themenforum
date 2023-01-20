package de.hsos.swa.application.port.input.getPostById;

import de.hsos.swa.application.port.input._shared.Result;

import javax.validation.Valid;

public interface GetPostByIdInputPort {
    Result<GetPostByIdInputPortResponse> getPostById(@Valid GetPostByIdInputPortRequest request);
}
