package de.hsos.swa.application.use_case;

import de.hsos.swa.application.port.input._shared.Result;
import de.hsos.swa.application.port.input.getPostById.GetPostByIdInputPort;
import de.hsos.swa.application.port.input.getPostById.GetPostByIdInputPortRequest;
import de.hsos.swa.application.port.input.getPostById.GetPostByIdInputPortResponse;
import de.hsos.swa.application.port.output.post.getPostById.GetPostByIdOutputPort;
import de.hsos.swa.application.port.output.post.getPostById.GetPostByIdOutputPortRequest;
import de.hsos.swa.application.port.output.post.getPostById.GetPostByIdOutputPortResponse;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

@RequestScoped
public class GetPostByIdUseCase implements GetPostByIdInputPort {

    @Inject
    GetPostByIdOutputPort getPostById;

    @Override
    public Result<GetPostByIdInputPortResponse> getPostById(GetPostByIdInputPortRequest request) {
        Result<GetPostByIdOutputPortResponse> response = getPostById.getPostById(new GetPostByIdOutputPortRequest(request.getId()));
        if(response.isSuccessful()) {
            return Result.success(new GetPostByIdInputPortResponse(response.getData().getPost().getTitle(), response.getData().getPost().getUser().getName()));
        }
        return Result.error("Cannot find Post");
    }
}
