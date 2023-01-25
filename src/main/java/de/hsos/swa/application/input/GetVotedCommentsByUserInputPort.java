package de.hsos.swa.application.input;

import de.hsos.swa.application.input.dto.in.GetVotedCommentsByUserInputPortRequest;
import de.hsos.swa.application.util.Result;
import de.hsos.swa.domain.entity.Comment;

import javax.validation.Valid;
import java.util.List;

public interface GetVotedCommentsByUserInputPort {
    Result<List<Comment>> getVotedCommentsByUser(@Valid GetVotedCommentsByUserInputPortRequest request);
}
