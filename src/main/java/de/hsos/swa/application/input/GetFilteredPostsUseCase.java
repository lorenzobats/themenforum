package de.hsos.swa.application.input;

import de.hsos.swa.application.annotations.InputPort;
import de.hsos.swa.application.input.dto.in.GetFilteredPostQuery;
import de.hsos.swa.application.input.dto.out.Result;
import de.hsos.swa.domain.entity.Post;

import javax.validation.Valid;
import java.util.List;
@InputPort
public interface GetFilteredPostsUseCase {
    Result<List<Post>> getFilteredPosts(@Valid GetFilteredPostQuery request);
}
