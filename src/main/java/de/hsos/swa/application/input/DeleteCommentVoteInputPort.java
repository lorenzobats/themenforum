package de.hsos.swa.application.input;

import de.hsos.swa.application.input.dto.in.DeleteCommentVoteInputPortRequest;
import de.hsos.swa.application.util.Result;
import de.hsos.swa.domain.entity.Comment;

import javax.validation.Valid;

public interface DeleteCommentVoteInputPort {
    Result<Comment> deleteCommentVote(@Valid DeleteCommentVoteInputPortRequest request);
}
