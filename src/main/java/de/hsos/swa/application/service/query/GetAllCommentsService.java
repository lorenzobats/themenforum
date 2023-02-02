package de.hsos.swa.application.service.query;

import de.hsos.swa.application.annotations.ApplicationService;
import de.hsos.swa.application.input.GetAllCommentsUseCase;
import de.hsos.swa.application.input.dto.out.ApplicationResult;
import de.hsos.swa.application.output.repository.CommentRepository;
import de.hsos.swa.domain.entity.Comment;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;

@RequestScoped
@Transactional(Transactional.TxType.REQUIRES_NEW)
@ApplicationService
public class GetAllCommentsService implements GetAllCommentsUseCase {
    @Inject
    CommentRepository commentRepository;

    @Override
    public ApplicationResult<List<Comment>> getAllComments(boolean includeReplies) {
        de.hsos.swa.application.output.repository.dto.out.RepositoryResult<List<Comment>> commentsResult = commentRepository.getAllComments(includeReplies);
        if (commentsResult.ok()) {
            return ApplicationResult.success(commentsResult.get());
        }

        return ApplicationResult.error("Cannot find Comments");
    }
}
