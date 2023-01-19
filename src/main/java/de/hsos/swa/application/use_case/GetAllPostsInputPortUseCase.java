package de.hsos.swa.application.use_case;

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
        Result<List<Post>> postResult = postRepository.getAllPosts(request.includeComments());
        if (postResult.isSuccessful()) {
            GetAllPostsInputPortResponse getAllPostsInputPortResponse = new GetAllPostsInputPortResponse(postResult.getData());
            return Result.success(getAllPostsInputPortResponse);
        }
        return Result.error("Cannot find Posts");
    }
}
