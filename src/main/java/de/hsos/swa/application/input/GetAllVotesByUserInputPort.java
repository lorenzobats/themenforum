package de.hsos.swa.application.input;

import de.hsos.swa.application.util.Result;
import de.hsos.swa.domain.value_object.Vote;

import java.util.List;

public interface GetAllVotesByUserInputPort {
    Result<List<Vote>> getAllVotesByUser(String username);
}
