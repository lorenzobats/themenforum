package de.hsos.swa.application;

import de.hsos.swa.application.input.GetFilteredPostsInputPort;
import de.hsos.swa.application.input.dto.in.GetFilteredPostInputPortRequest;
import de.hsos.swa.application.output.repository.PostRepository;
import de.hsos.swa.application.util.Result;
import de.hsos.swa.domain.entity.Post;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;

@RequestScoped
@Transactional
public class GetFilteredPostsInputPortUseCase implements GetFilteredPostsInputPort {

    @Inject
    PostRepository postRepository;

    @Override
    public Result<List<Post>> getFilteredPosts(GetFilteredPostInputPortRequest request) {
        Result<List<Post>> postsResult = postRepository.getAllFilteredPosts(request.filterParams(), request.includeComments());
        if (postsResult.isSuccessful()) {
            return Result.isSuccessful(postsResult.getData());
        }
        return Result.error("Cannot find Posts");
    }
}
