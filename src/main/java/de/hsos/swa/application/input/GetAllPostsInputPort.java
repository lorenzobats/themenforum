package de.hsos.swa.application.input;

import de.hsos.swa.application.use_case_query.SortingParams;
import de.hsos.swa.application.util.Result;
import de.hsos.swa.domain.entity.Post;

import java.util.List;

public interface GetAllPostsInputPort {
    Result<List<Post>> getAllPosts(boolean includeComments);
}
