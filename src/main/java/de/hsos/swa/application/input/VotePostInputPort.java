package de.hsos.swa.application.input;
import de.hsos.swa.application.util.Result;
import de.hsos.swa.application.input.dto.in.VotePostInputPortRequest;
import de.hsos.swa.domain.entity.Post;
import de.hsos.swa.domain.entity.Vote;

import javax.validation.Valid;

public interface VotePostInputPort {
   Result<Vote> votePost(@Valid VotePostInputPortRequest request);
}
