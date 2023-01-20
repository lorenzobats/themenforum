package de.hsos.swa.application.port.input;

import de.hsos.swa.application.port.input.request.ReplyToCommentInputPortRequest;

import javax.validation.Valid;
import java.util.UUID;

public interface ReplyToCommentInputPort {
    Result<UUID> replyToComment(@Valid ReplyToCommentInputPortRequest request);
}
