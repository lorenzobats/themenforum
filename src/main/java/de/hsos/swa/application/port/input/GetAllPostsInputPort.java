package de.hsos.swa.application.port.input;

import de.hsos.swa.application.port.input.request.GetAllPostsInputPortRequest;
import de.hsos.swa.domain.entity.Post;

import javax.validation.Valid;
import java.util.List;

public interface GetAllPostsInputPort {
    Result<List<Post>> getAllPosts(@Valid GetAllPostsInputPortRequest request);
}
