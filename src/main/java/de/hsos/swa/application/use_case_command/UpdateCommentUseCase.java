package de.hsos.swa.application.use_case_command;

import de.hsos.swa.application.input.UpdateCommentInputPort;
import de.hsos.swa.application.input.dto.in.UpdateCommentInputPortRequest;
import de.hsos.swa.application.util.Result;
import de.hsos.swa.domain.entity.Comment;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.transaction.Transactional;

@RequestScoped
@Transactional(Transactional.TxType.REQUIRES_NEW)
public class UpdateCommentUseCase implements UpdateCommentInputPort {
    @Override
    public Result<Comment> updateComment(UpdateCommentInputPortRequest request) {
        return null;
    }
}
