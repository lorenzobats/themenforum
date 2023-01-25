package de.hsos.swa.application.use_case_query;

import de.hsos.swa.application.input.GetVotedCommentsByUserInputPort;
import de.hsos.swa.application.input.dto.in.GetVotedCommentsByUserInputPortRequest;
import de.hsos.swa.application.util.Result;
import de.hsos.swa.domain.entity.Comment;

import java.util.List;

public class GetVotedCommentsByUserUseCase implements GetVotedCommentsByUserInputPort {

    @Override
    public Result<List<Comment>> getVotedCommentsByUser(GetVotedCommentsByUserInputPortRequest request) {
        return null;
    }
}
