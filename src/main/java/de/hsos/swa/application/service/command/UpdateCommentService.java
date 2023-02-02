package de.hsos.swa.application.service.command;

import de.hsos.swa.application.annotations.ApplicationService;
import de.hsos.swa.application.input.UpdateCommentUseCase;
import de.hsos.swa.application.input.dto.in.UpdateCommentCommand;
import de.hsos.swa.application.input.dto.out.ApplicationResult;
import de.hsos.swa.domain.entity.Comment;

import javax.enterprise.context.RequestScoped;
import javax.transaction.Transactional;

@RequestScoped
@Transactional(Transactional.TxType.REQUIRES_NEW)
@ApplicationService
public class UpdateCommentService implements UpdateCommentUseCase {
    @Override
    public ApplicationResult<Comment> updateComment(UpdateCommentCommand request) {
        // TODO: Implementieren
        return null;
    }
}
