package de.hsos.swa.application.input;

import de.hsos.swa.application.input.dto.in.DeleteCommentInputPortRequest;
import de.hsos.swa.application.util.Result;
import de.hsos.swa.domain.entity.Comment;

import javax.validation.Valid;

public interface DeleteCommentInputPort {
    Result<Comment> deleteComment(@Valid DeleteCommentInputPortRequest request);
}