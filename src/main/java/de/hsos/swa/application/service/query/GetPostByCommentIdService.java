package de.hsos.swa.application.service.query;

import de.hsos.swa.application.annotations.ApplicationService;
import de.hsos.swa.application.input.GetPostByCommentIdUseCase;
import de.hsos.swa.application.input.dto.in.GetPostByCommentIdQuery;
import de.hsos.swa.application.input.dto.out.Result;
import de.hsos.swa.application.output.repository.PostRepository;
import de.hsos.swa.application.output.repository.dto.out.RepositoryResult;
import de.hsos.swa.domain.entity.Post;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.UUID;

@RequestScoped
@Transactional(Transactional.TxType.REQUIRES_NEW)
@ApplicationService
public class GetPostByCommentIdService implements GetPostByCommentIdUseCase {

    @Inject
    PostRepository postRepository;

    @Override
    public Result<Post> getPostByCommentId(GetPostByCommentIdQuery request) {
        RepositoryResult<Post> postResult = postRepository.getPostByCommentId(UUID.fromString(request.id()));
        if (postResult.ok()) {
            return Result.success(postResult.get());
        }
        return Result.error("Cannot find Post");
    }
}
