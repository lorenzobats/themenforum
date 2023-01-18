package de.hsos.swa.application.port.input.replyToComment;

import de.hsos.swa.application.port.input._shared.Result;

public interface ReplyToCommentInputPort {
    Result<ReplyToCommentInputPortResponse> replyToComment(ReplyToCommentInputPortRequest request);
}
