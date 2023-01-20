package de.hsos.swa.application.use_case;

import de.hsos.swa.application.Result;
import de.hsos.swa.application.input.GetPostByIdInputPort;
import de.hsos.swa.application.input.request.GetPostByIdInputPortRequest;
import de.hsos.swa.application.output.persistence.PostRepository;
import de.hsos.swa.domain.entity.Post;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.util.UUID;

@RequestScoped
public class GetPostByIdUseCase implements GetPostByIdInputPort {

    @Inject
    PostRepository postRepository;

    @Override
    public Result<Post> getPostById(GetPostByIdInputPortRequest request) {
        Result<Post> postResult = postRepository.getPostById(UUID.fromString(request.getId()), request.includeComments());
        if (postResult.isSuccessful()) {
            return Result.success(postResult.getData());
        }
        return Result.error("Cannot find Post");
    }
}
