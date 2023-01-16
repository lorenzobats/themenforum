package de.hsos.swa.application.use_case;

import de.hsos.swa.application.port.input._shared.Result;
import de.hsos.swa.application.port.input.getPostById.GetPostByIdInputPort;
import de.hsos.swa.application.port.input.getPostById.GetPostByIdInputPortRequest;
import de.hsos.swa.application.port.input.getPostById.GetPostByIdInputPortResponse;
import de.hsos.swa.application.port.output.post.GetPostByIdOutputPort;
import de.hsos.swa.domain.entity.Post;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.util.UUID;

@RequestScoped
public class GetPostByIdUseCase implements GetPostByIdInputPort {

    @Inject
    GetPostByIdOutputPort getPostById;

    @Override
    public Result<GetPostByIdInputPortResponse> getPostById(GetPostByIdInputPortRequest request) {
        Result<Post> response = getPostById.getPostById(UUID.fromString(request.getId()));
        if(response.isSuccessful()) {
            // TODO: PostDTO zurückgeben
            return Result.success(new GetPostByIdInputPortResponse(response.getData().getTitle(), response.getData().getUser().getName()));
        }
        return Result.error("Cannot find Post");
    }
}
