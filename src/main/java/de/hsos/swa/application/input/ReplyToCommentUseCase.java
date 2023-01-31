package de.hsos.swa.application.input;

import de.hsos.swa.application.annotations.InputPort;
import de.hsos.swa.application.input.dto.out.Result;
import de.hsos.swa.application.input.dto.in.ReplyToCommentCommand;
import de.hsos.swa.domain.entity.Comment;

import javax.validation.Valid;
@InputPort
public interface ReplyToCommentUseCase {
    Result<Comment> replyToComment(@Valid ReplyToCommentCommand request);
}
