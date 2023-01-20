package de.hsos.swa.application.input;

import de.hsos.swa.application.Result;
import de.hsos.swa.application.input.request.CommentPostInputPortRequest;
import de.hsos.swa.domain.entity.Comment;

import javax.validation.Valid;

public interface CommentPostInputPort {
   Result<Comment> commentPost(@Valid CommentPostInputPortRequest request);
}
