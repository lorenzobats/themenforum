package de.hsos.swa.application.use_case_query;

import de.hsos.swa.application.input.GetPostByCommentIdInputPort;
import de.hsos.swa.application.input.GetPostByIdInputPort;
import de.hsos.swa.application.input.dto.in.GetPostByCommentIdInputPortRequest;
import de.hsos.swa.application.input.dto.in.GetPostByIdInputPortRequest;
import de.hsos.swa.application.output.repository.PostRepository;
import de.hsos.swa.application.util.Result;
import de.hsos.swa.domain.entity.Post;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.util.UUID;

@RequestScoped
public class GetPostByCommentIdUseCase implements GetPostByCommentIdInputPort {

    @Inject
    PostRepository postRepository;

    @Override
    public Result<Post> getPostByCommentId(GetPostByCommentIdInputPortRequest request) {
        Result<Post> postResult = postRepository.getPostByCommentId(UUID.fromString(request.id()));
        if (postResult.isSuccessful()) {
            return Result.success(postResult.getData());
        }
        return Result.error("Cannot find Post");
    }
}
