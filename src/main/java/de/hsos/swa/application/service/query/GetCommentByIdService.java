package de.hsos.swa.application.service.query;

import de.hsos.swa.application.annotations.ApplicationService;
import de.hsos.swa.application.input.GetCommentByIdUseCase;
import de.hsos.swa.application.input.dto.in.GetCommentByIdQuery;
import de.hsos.swa.application.input.dto.out.ApplicationResult;
import de.hsos.swa.application.output.repository.CommentRepository;
import de.hsos.swa.application.output.repository.dto.in.RepositoryResult;
import de.hsos.swa.domain.entity.Comment;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.UUID;

@RequestScoped
@Transactional(Transactional.TxType.REQUIRES_NEW)
@ApplicationService
public class GetCommentByIdService implements GetCommentByIdUseCase {

    @Inject
    CommentRepository commentRepository;

    @Override
    public ApplicationResult<Comment> getCommentById(GetCommentByIdQuery query) {
        RepositoryResult<Comment> result = commentRepository.getCommentById(UUID.fromString(query.commentId()), true);
        if (result.error()) {
            if (result.status() == RepositoryResult.Status.ENTITY_NOT_FOUND) {
                return ApplicationResult.notFound("Cannot find aomment: " + query.commentId());
            }
            return ApplicationResult.exception();
        }

        return ApplicationResult.ok(result.get());
    }
}
