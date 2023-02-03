package de.hsos.swa.application.service.query;

import de.hsos.swa.application.annotations.ApplicationService;
import de.hsos.swa.application.input.GetAllCommentsUseCase;
import de.hsos.swa.application.input.dto.in.GetAllCommentsQuery;
import de.hsos.swa.application.input.dto.out.ApplicationResult;
import de.hsos.swa.application.output.repository.CommentRepository;
import de.hsos.swa.application.output.repository.dto.out.RepositoryResult;
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
    public ApplicationResult<List<Comment>> getAllComments(GetAllCommentsQuery query) {
        RepositoryResult<List<Comment>> result = commentRepository.getAllComments(query.includeReplies());
        if (result.error())
            return ApplicationResult.exception();

        return ApplicationResult.ok(result.get());
    }
}
