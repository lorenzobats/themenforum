package de.hsos.swa.application.port.input.commentPost;

import de.hsos.swa.application.port.input._shared.Result;

import javax.validation.Valid;

public interface CommentPostInputPort {
   Result<CommentPostInputPortResponse> commentPost(@Valid CommentPostInputPortRequest command);
}
