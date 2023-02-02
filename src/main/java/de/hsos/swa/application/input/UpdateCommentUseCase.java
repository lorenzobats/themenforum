package de.hsos.swa.application.input;

import de.hsos.swa.application.annotations.InputPort;
import de.hsos.swa.application.input.dto.in.UpdateCommentCommand;
import de.hsos.swa.application.input.dto.out.ApplicationResult;
import de.hsos.swa.domain.entity.Comment;

import javax.validation.Valid;
@InputPort
public interface UpdateCommentUseCase {
   ApplicationResult<Comment> updateComment(@Valid UpdateCommentCommand request);
}
