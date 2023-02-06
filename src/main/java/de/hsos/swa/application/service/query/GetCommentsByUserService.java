package de.hsos.swa.application.service.query;

import de.hsos.swa.application.annotations.ApplicationService;
import de.hsos.swa.application.input.query.GetCommentsByUserUseCase;
import de.hsos.swa.application.input.dto.in.GetCommentsByUserQuery;
import de.hsos.swa.application.input.dto.out.ApplicationResult;
import de.hsos.swa.application.output.repository.CommentRepository;
import de.hsos.swa.application.output.repository.dto.in.RepositoryResult;
import de.hsos.swa.domain.entity.Comment;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;

@RequestScoped
@Transactional(Transactional.TxType.REQUIRES_NEW)
@ApplicationService
public class GetCommentsByUserService implements GetCommentsByUserUseCase {

    @Inject
    CommentRepository commentRepository;

    @Override
    public ApplicationResult<List<Comment>> getCommentsByUser(GetCommentsByUserQuery query) {
        RepositoryResult<List<Comment>> result = commentRepository.getCommentsByUser(query.username());
        if (result.error())
            return ApplicationResult.exception();

        return ApplicationResult.ok(result.get());
    }
}
