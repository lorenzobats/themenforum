package de.hsos.swa.application.input;
import de.hsos.swa.application.Result;
import de.hsos.swa.application.input.request.VoteCommentInputPortRequest;
import de.hsos.swa.domain.entity.Comment;

import javax.validation.Valid;

public interface VoteCommentInputPort {
   Result<Comment> voteComment(@Valid VoteCommentInputPortRequest request);
}
