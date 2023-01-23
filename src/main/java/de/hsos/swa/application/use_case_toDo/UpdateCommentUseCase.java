package de.hsos.swa.application.use_case_toDo;

import de.hsos.swa.application.input.UpdateCommentInputPort;
import de.hsos.swa.application.input.dto.in.UpdateCommentInputPortRequest;
import de.hsos.swa.application.util.Result;
import de.hsos.swa.domain.entity.Comment;

public class UpdateCommentUseCase implements UpdateCommentInputPort {
    @Override
    public Result<Comment> updateComment(UpdateCommentInputPortRequest request) {
        return null;
    }
}
