package de.hsos.swa.application.input;
import de.hsos.swa.application.util.Result;
import de.hsos.swa.application.input.dto.in.VoteEntityInputPortRequest;
import de.hsos.swa.domain.entity.Vote;

import javax.validation.Valid;

public interface VoteEntityInputPort {
   Result<Vote> vote(@Valid VoteEntityInputPortRequest request);
}
