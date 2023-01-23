package de.hsos.swa.application.input;

import de.hsos.swa.application.input.dto.in.DeletePostVoteInputPortRequest;
import de.hsos.swa.application.util.Result;
import de.hsos.swa.domain.entity.Post;

import javax.validation.Valid;

public interface DeletePostVoteInputPort {
    Result<Post> deletePostVote(@Valid DeletePostVoteInputPortRequest request);
}
