package de.hsos.swa.application.output.repository;

import de.hsos.swa.application.output.dto.VotePersistenceDto;
import de.hsos.swa.application.util.Result;

import java.util.UUID;

public interface VoteRepository {
    // READ
    Result<VotePersistenceDto> getVoteById(UUID voteId);
    // TODO: GetALlVotes
}
