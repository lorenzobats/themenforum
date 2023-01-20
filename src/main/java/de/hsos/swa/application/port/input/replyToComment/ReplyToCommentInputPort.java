package de.hsos.swa.application.port.input.replyToComment;

import de.hsos.swa.application.port.input._shared.Result;

import javax.validation.Valid;

public interface ReplyToCommentInputPort {
    Result<ReplyToCommentInputPortResponse> replyToComment(@Valid ReplyToCommentInputPortRequest request);
}
