package de.hsos.swa.application.port.input.getAllPosts;

import de.hsos.swa.application.port.input._shared.Result;

public interface GetAllPostsInputPort {
    Result<GetAllPostsInputPortResponse> getAllPosts(GetAllPostsInputPortRequest request);
}
