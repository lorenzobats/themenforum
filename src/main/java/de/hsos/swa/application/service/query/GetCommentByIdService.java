package de.hsos.swa.application.service.query;

import de.hsos.swa.application.annotations.ApplicationService;
import de.hsos.swa.application.input.GetCommentByIdUseCase;
import de.hsos.swa.application.input.dto.in.GetCommentByIdQuery;
import de.hsos.swa.application.input.dto.out.ApplicationResult;
import de.hsos.swa.application.output.repository.CommentRepository;
import de.hsos.swa.application.output.repository.dto.out.RepositoryResult;
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
    public ApplicationResult<Comment> getCommentById(GetCommentByIdQuery request) {
        RepositoryResult<Comment> commentResult = commentRepository.getCommentById(UUID.fromString(request.id()), true);
        if (commentResult.ok()) {
            return ApplicationResult.success(commentResult.get());
        }
        return ApplicationResult.error("Cannot find Comment");
    }
}
