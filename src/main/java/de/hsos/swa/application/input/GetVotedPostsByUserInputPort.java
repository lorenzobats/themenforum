package de.hsos.swa.application.input;

import de.hsos.swa.application.input.dto.in.GetVotedCommentsByUserInputPortRequest;
import de.hsos.swa.application.input.dto.in.GetVotedPostsByUserInputPortRequest;
import de.hsos.swa.application.util.Result;
import de.hsos.swa.domain.entity.Post;

import javax.validation.Valid;
import java.util.List;

public interface GetVotedPostsByUserInputPort {
    Result<List<Post>> getVotedPostsByUser(@Valid GetVotedPostsByUserInputPortRequest request);
}
