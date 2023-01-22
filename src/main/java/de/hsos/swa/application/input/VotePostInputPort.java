package de.hsos.swa.application.input;
import de.hsos.swa.application.Result;
import de.hsos.swa.application.input.request.VotePostInputPortRequest;
import de.hsos.swa.domain.entity.Post;

import javax.validation.Valid;

public interface VotePostInputPort {
   Result<Post> votePost(@Valid VotePostInputPortRequest request);
}
