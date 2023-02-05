package de.hsos.swa.application.service.query;

import de.hsos.swa.application.annotations.ApplicationService;
import de.hsos.swa.application.input.GetPostByCommentIdUseCase;
import de.hsos.swa.application.input.dto.in.GetPostByCommentIdQuery;
import de.hsos.swa.application.input.dto.out.ApplicationResult;
import de.hsos.swa.application.output.repository.PostRepository;
import de.hsos.swa.application.output.repository.dto.in.RepositoryResult;
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
    public ApplicationResult<Post> getPostByCommentId(GetPostByCommentIdQuery query) {
        RepositoryResult<Post> result = postRepository.getPostByCommentId(UUID.fromString(query.id()));
        if (result.error()) {
            if (result.status() == RepositoryResult.Status.ENTITY_NOT_FOUND) {
                return ApplicationResult.notFound("Cannot find post for comment: " + query.id());
            }
            return ApplicationResult.exception();
        }
        return ApplicationResult.ok(result.get());
    }
}
