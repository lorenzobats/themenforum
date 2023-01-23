package de.hsos.swa.application.use_case_toDo;

import de.hsos.swa.application.input.DeleteCommentInputPort;
import de.hsos.swa.application.input.dto.in.DeleteCommentInputPortRequest;
import de.hsos.swa.application.util.Result;
import de.hsos.swa.domain.entity.Comment;

public class DeleteCommentUseCase implements DeleteCommentInputPort {

    @Override
    public Result<Comment> deleteComment(DeleteCommentInputPortRequest request) {
        return null;
    }
}
