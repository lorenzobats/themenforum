package de.hsos.swa.application.use_case_query;

import de.hsos.swa.application.input.GetAllCommentsInputPort;
import de.hsos.swa.application.output.repository.CommentRepository;
import de.hsos.swa.application.util.Result;
import de.hsos.swa.domain.entity.Comment;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;

@RequestScoped
@Transactional(Transactional.TxType.REQUIRES_NEW)
public class GetAllCommentsUseCase implements GetAllCommentsInputPort {
    @Inject
    CommentRepository commentRepository;

    @Override
    public Result<List<Comment>> getAllComments(boolean includeReplies) {
        Result<List<Comment>> commentsResult = commentRepository.getAllComments(includeReplies);
        if (commentsResult.isSuccessful()) {
            return Result.success(commentsResult.getData());
        }
        return Result.error("Cannot find Comments");
    }
}
