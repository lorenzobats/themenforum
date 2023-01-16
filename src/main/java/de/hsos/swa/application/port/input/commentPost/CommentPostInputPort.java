package de.hsos.swa.application.port.input.commentPost;

import de.hsos.swa.application.port.input._shared.Result;

public interface CommentPostInputPort {
   Result<CommentPostInputPortResult> commentPost(CommentPostInputPortCommand command);
}
