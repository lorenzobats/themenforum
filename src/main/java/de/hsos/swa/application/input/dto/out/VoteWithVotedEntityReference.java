package de.hsos.swa.application.input.dto.out;

import de.hsos.swa.domain.entity.Vote;
import de.hsos.swa.domain.vo.VotedEntityType;

import java.util.UUID;

/**
 * Die Klasse VoteWithVotedEntityReference ermöglicht die Rückgabe eines Votes und zusätzlich den Typ der gevoteten
 * Entität (Post oder Comment), sowie die ID der gevoteten Entität.
 *
 * @author Lorenzo Battiston
 * @author Oliver Schlüter
 * @version 1.0
 */
public class VoteWithVotedEntityReference {
    public final Vote vote;
    public final  VotedEntityType votedEntityType;
    public final UUID votedEntityId;

    public VoteWithVotedEntityReference(Vote vote, VotedEntityType votedEntityType, UUID votedEntityId) {
        this.vote = vote;
        this.votedEntityType = votedEntityType;
        this.votedEntityId = votedEntityId;
    }
}
