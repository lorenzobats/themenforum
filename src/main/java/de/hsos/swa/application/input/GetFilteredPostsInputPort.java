package de.hsos.swa.application.input;

import de.hsos.swa.application.input.dto.in.GetFilteredPostInputPortRequest;
import de.hsos.swa.application.util.Result;
import de.hsos.swa.domain.entity.Post;

import javax.validation.Valid;
import java.util.List;

public interface GetFilteredPostsInputPort {
    Result<List<Post>> getFilteredPosts(@Valid GetFilteredPostInputPortRequest request);
}
