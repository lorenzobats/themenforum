package de.hsos.swa.application.input;

import de.hsos.swa.application.input.dto.in.UpdateCommentInputPortRequest;
import de.hsos.swa.application.util.Result;
import de.hsos.swa.domain.entity.Comment;

import javax.validation.Valid;

public interface UpdateCommentInputPort {
   Result<Comment> updateComment(@Valid UpdateCommentInputPortRequest request);
}
