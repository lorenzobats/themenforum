package de.hsos.swa.application.use_case_query;

import de.hsos.swa.application.input.GetCommentByIdInputPort;
import de.hsos.swa.application.input.dto.in.GetCommentByIdInputPortRequest;
import de.hsos.swa.application.output.repository.CommentRepository;
import de.hsos.swa.application.output.repository.RepositoryResult;
import de.hsos.swa.application.util.Result;
import de.hsos.swa.domain.entity.Comment;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.UUID;

@RequestScoped
@Transactional(Transactional.TxType.REQUIRES_NEW)
public class GetCommentByIdUseCase implements GetCommentByIdInputPort {

    @Inject
    CommentRepository commentRepository;

    @Override
    public Result<Comment> getCommentById(GetCommentByIdInputPortRequest request) {
        RepositoryResult<Comment> commentResult = commentRepository.getCommentById(UUID.fromString(request.id()), true);
        if (commentResult.ok()) {
            return Result.success(commentResult.get());
        }
        return Result.error("Cannot find Comment");
    }
}
