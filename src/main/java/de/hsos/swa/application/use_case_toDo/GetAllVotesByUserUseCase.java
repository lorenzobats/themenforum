package de.hsos.swa.application.use_case_toDo;

import de.hsos.swa.application.input.GetAllVotesByUserInputPort;
import de.hsos.swa.application.util.Result;
import de.hsos.swa.domain.value_object.Vote;

import java.util.List;

public class GetAllVotesByUserUseCase implements GetAllVotesByUserInputPort {

    @Override
    public Result<List<Vote>> getAllVotesByUser(String username) {
        return null;
    }
}
