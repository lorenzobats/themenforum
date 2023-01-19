package de.hsos.swa.application.use_case;

import de.hsos.swa.application.PostFilterParams;
import de.hsos.swa.application.port.input._shared.Result;
import de.hsos.swa.application.port.input.getAllPosts.GetAllPostsInputPort;
import de.hsos.swa.application.port.input.getAllPosts.GetAllPostsInputPortRequest;
import de.hsos.swa.application.port.input.getAllPosts.GetAllPostsInputPortResponse;
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
    public Result<GetAllPostsInputPortResponse> getAllPosts(GetAllPostsInputPortRequest request) {
        // TODO: Validierung der FilterParameter in Application Service?
        Result<List<Post>> postResult = postRepository.getAllPosts((boolean) request.getFilterParams().get(PostFilterParams.INCLUDE_COMMENTS));
        // TODO Frage Filtern der weiteren Parameter in Domain Service oder durch Datenbank?
        if (postResult.isSuccessful()) {
            GetAllPostsInputPortResponse getAllPostsInputPortResponse = new GetAllPostsInputPortResponse(postResult.getData());
            return Result.success(getAllPostsInputPortResponse);
        }
        return Result.error("Cannot find Posts");
    }
}
