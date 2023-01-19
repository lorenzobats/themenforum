package de.hsos.swa.application.use_case;

import de.hsos.swa.application.port.input._shared.Result;
import de.hsos.swa.application.port.input.getPostById.GetPostByIdInputPort;
import de.hsos.swa.application.port.input.getPostById.GetPostByIdInputPortRequest;
import de.hsos.swa.application.port.input.getPostById.GetPostByIdInputPortResponse;
import de.hsos.swa.application.port.output.PostRepository;
import de.hsos.swa.domain.entity.Post;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.util.UUID;

@RequestScoped
public class GetPostByIdUseCase implements GetPostByIdInputPort {

    @Inject
    PostRepository postRepository;

    @Override
    public Result<GetPostByIdInputPortResponse> getPostById(GetPostByIdInputPortRequest request) {
        Result<Post> postResult = postRepository.getPostById(UUID.fromString(request.getId()), request.includeComments());
        if (postResult.isSuccessful()) {
            // TODO: PostDTO zurückgeben

            GetPostByIdInputPortResponse getPostByIdInputPortResponse = new GetPostByIdInputPortResponse(
                    postResult.getData().getTitle(),
                    postResult.getData().getUser().getName());

            getPostByIdInputPortResponse.setComments(postResult.getData().getComments());

            return Result.success(getPostByIdInputPortResponse);
        }
        return Result.error("Cannot find Post");
    }
}
