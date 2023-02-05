package de.hsos.swa.application.output.repository;

import de.hsos.swa.application.output.repository.dto.in.VoteQueryDto;
import de.hsos.swa.application.output.repository.dto.in.RepositoryResult;

import java.util.List;
import java.util.UUID;

/**
 *
 */
public interface VoteRepository {

    //------------------------------------------------------------------------------------------------------------------
    // QUERIES
    RepositoryResult<VoteQueryDto> getVoteById(UUID voteId);
    RepositoryResult<List<VoteQueryDto>> getAllVotesByUser(String username);
    RepositoryResult<List<VoteQueryDto>> getAllVotes();
}
