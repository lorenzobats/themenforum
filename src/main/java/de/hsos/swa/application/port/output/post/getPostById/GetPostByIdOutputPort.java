package de.hsos.swa.application.port.output.post.getPostById;

import de.hsos.swa.application.port.input._shared.Result;

public interface GetPostByIdOutputPort {
    Result<GetPostByIdOutputPortResponse> getPostById(GetPostByIdOutputPortRequest request);
}
