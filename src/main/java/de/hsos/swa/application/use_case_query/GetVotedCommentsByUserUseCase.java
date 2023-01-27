package de.hsos.swa.application.use_case_query;

import de.hsos.swa.application.input.GetVotedCommentsByUserInputPort;
import de.hsos.swa.application.input.dto.in.GetVotedCommentsByUserInputPortRequest;
import de.hsos.swa.application.util.Result;
import de.hsos.swa.domain.entity.Comment;

import javax.enterprise.context.RequestScoped;
import javax.transaction.Transactional;
import java.util.List;

@RequestScoped
@Transactional(Transactional.TxType.REQUIRES_NEW)
public class GetVotedCommentsByUserUseCase implements GetVotedCommentsByUserInputPort {

    @Override
    public Result<List<Comment>> getVotedCommentsByUser(GetVotedCommentsByUserInputPortRequest request) {
        return null;
    }
}
