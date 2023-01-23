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
    public Result<List<Post>> getAllPosts(GetAllPostsInputPortRequest request) {
        // TODO: Validierung der FilterParameter in Application Service?
        //Result<List<Post>> postsResult = postRepository.getAllPosts((boolean) request.getFilterParams().get(PostFilterParams.INCLUDE_COMMENTS));
        Result<List<Post>> postsResult = postRepository.getAllFilteredPosts(request.getFilterParams(), request.getIncludeComments());
        // TODO Frage Filtern der weiteren Parameter in Domain Service oder durch Datenbank?
        if (postsResult.isSuccessful()) {
            return Result.success(postsResult.getData());
        }
        return Result.error("Cannot find Posts");
    }
}
