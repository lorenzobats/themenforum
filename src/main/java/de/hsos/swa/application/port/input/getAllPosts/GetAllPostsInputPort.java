package de.hsos.swa.application.port.input.getAllPosts;

import de.hsos.swa.application.port.input._shared.Result;

import javax.validation.Valid;

public interface GetAllPostsInputPort {
    Result<GetAllPostsInputPortResponse> getAllPosts(@Valid GetAllPostsInputPortRequest request);
}
