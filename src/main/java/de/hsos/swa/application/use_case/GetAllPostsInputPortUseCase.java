package de.hsos.swa.application.use_case;

import de.hsos.swa.application.PostFilterParams;
import de.hsos.swa.application.port.input.Result;
import de.hsos.swa.application.port.input.GetAllPostsInputPort;
import de.hsos.swa.application.port.input.request.GetAllPostsInputPortRequest;
import de.hsos.swa.application.port.output.PostRepository;
import de.hsos.swa.domain.entity.Post;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.util.List;

@RequestScoped
public class GetAllPostsInputPortUseCase implements GetAllPostsInputPort {

    @Inject
    PostRepository postRepository;

    @Override
    public Result<List<Post>> getAllPosts(GetAllPostsInputPortRequest request) {
        // TODO: Validierung der FilterParameter in Application Service?
        Result<List<Post>> postsResult = postRepository.getAllPosts((boolean) request.getFilterParams().get(PostFilterParams.INCLUDE_COMMENTS));
        // TODO Frage Filtern der weiteren Parameter in Domain Service oder durch Datenbank?
        if (postsResult.isSuccessful()) {
            return Result.success(postsResult.getData());
        }
        return Result.error("Cannot find Posts");
    }
}
