package de.hsos.swa.application;

import de.hsos.swa.application.input.GetAllPostsInputPort;
import de.hsos.swa.application.input.dto.in.GetAllPostsInputPortRequest;
import de.hsos.swa.application.output.repository.PostRepository;
import de.hsos.swa.application.util.Result;
import de.hsos.swa.domain.entity.Post;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;

@RequestScoped
@Transactional
public class GetAllPostsInputPortUseCase implements GetAllPostsInputPort {

    @Inject
    PostRepository postRepository;

    @Override
    public Result<List<Post>> getFilteredPosts(GetAllPostsInputPortRequest request) {
        Result<List<Post>> postsResult = postRepository.getAllFilteredPosts(request.getFilterParams(), request.getIncludeComments());
        if (postsResult.isSuccessful()) {
            return Result.success(postsResult.getData());
        }
        return Result.error("Cannot find Posts");
    }

    @Override
    public Result<List<Post>> getAllPosts(boolean includeComments) {
        Result<List<Post>> postsResult = postRepository.getAllPosts(includeComments);
        if (postsResult.isSuccessful()) {
            return Result.success(postsResult.getData());
        }
        return Result.error("Cannot find Posts");
    }
}
