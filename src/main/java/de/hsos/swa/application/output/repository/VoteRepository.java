package de.hsos.swa.application.output.repository;

import de.hsos.swa.application.output.repository.dto.in.VoteQueryDto;
import de.hsos.swa.application.output.repository.dto.out.RepositoryResult;

import java.util.List;
import java.util.UUID;

/**
 * TODO: JavaDocs (Hinweis nur Queries)
 */
public interface VoteRepository {

    //------------------------------------------------------------------------------------------------------------------
    // QUERIES
    RepositoryResult<VoteQueryDto> getVoteById(UUID voteId);
    RepositoryResult<List<VoteQueryDto>> getAllVotesByUser(String username);
    RepositoryResult<List<VoteQueryDto>> getAllVotes();
}
