package de.hsos.swa.application.input;
import de.hsos.swa.application.annotations.InputPort;
import de.hsos.swa.application.input.dto.out.ApplicationResult;
import de.hsos.swa.application.input.dto.in.VoteEntityCommand;
import de.hsos.swa.domain.entity.Vote;

import javax.validation.Valid;
@InputPort
public interface VoteEntityUseCase {
   ApplicationResult<Vote> vote(@Valid VoteEntityCommand command, String requestingUser);
}
