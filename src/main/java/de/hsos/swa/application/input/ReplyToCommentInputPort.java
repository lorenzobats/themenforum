package de.hsos.swa.application.input;

import de.hsos.swa.application.output.Result;
import de.hsos.swa.application.input.dto.in.ReplyToCommentInputPortRequest;
import de.hsos.swa.domain.entity.Comment;

import javax.validation.Valid;

public interface ReplyToCommentInputPort {
    Result<Comment> replyToComment(@Valid ReplyToCommentInputPortRequest request);
}
