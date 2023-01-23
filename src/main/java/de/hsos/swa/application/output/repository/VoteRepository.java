package de.hsos.swa.application.output.repository;

import de.hsos.swa.application.output.dto.VoteWithTypeAndParentID;
import de.hsos.swa.application.util.Result;

import java.util.List;
import java.util.UUID;

public interface VoteRepository {
    Result<List<VoteWithTypeAndParentID>> getAllVotesByUserId(UUID userId);
}
