package de.hsos.swa.application.input;

import de.hsos.swa.application.output.Result;
import de.hsos.swa.application.input.dto.in.CommentPostInputPortRequest;
import de.hsos.swa.domain.entity.Comment;

import javax.validation.Valid;

public interface CommentPostInputPort {
   Result<Comment> commentPost(@Valid CommentPostInputPortRequest request);
}
