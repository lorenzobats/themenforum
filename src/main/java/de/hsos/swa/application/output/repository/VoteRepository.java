package de.hsos.swa.application.output.repository;

import de.hsos.swa.application.output.dto.VotePersistenceDto;
import de.hsos.swa.application.util.Result;

import java.util.UUID;

public interface VoteRepository {
    Result<VotePersistenceDto> getVoteById(UUID voteId);
}
