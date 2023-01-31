package de.hsos.swa.application.output.repository;

import de.hsos.swa.application.output.repository.dto.in.VoteOutputPortDto;

import java.util.List;
import java.util.UUID;

public interface VoteRepository {
    // READ
    RepositoryResult<VoteOutputPortDto> getVoteById(UUID voteId);
    RepositoryResult<List<VoteOutputPortDto>> getAllVotesByUser(String username);

    RepositoryResult<List<VoteOutputPortDto>> getAllVotes();
}
