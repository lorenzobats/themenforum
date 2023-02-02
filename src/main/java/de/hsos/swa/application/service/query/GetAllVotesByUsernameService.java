package de.hsos.swa.application.service.query;

import de.hsos.swa.application.annotations.ApplicationService;
import de.hsos.swa.application.input.GetAllVotesByUsernameUseCase;
import de.hsos.swa.application.input.dto.in.GetAllVotesByUsernameQuery;
import de.hsos.swa.application.input.dto.out.ApplicationResult;
import de.hsos.swa.application.input.dto.out.VoteWithVotedEntityReferenceDto;
import de.hsos.swa.application.output.repository.dto.in.VoteQueryDto;
import de.hsos.swa.application.output.repository.VoteRepository;
import org.jboss.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.core.SecurityContext;
import java.util.List;

@RequestScoped
@Transactional(Transactional.TxType.REQUIRES_NEW)
@ApplicationService
public class GetAllVotesByUsernameService implements GetAllVotesByUsernameUseCase {

    @Inject
    VoteRepository voteRepository;
    @Inject
    Logger log;

    @Override
    public ApplicationResult<List<VoteWithVotedEntityReferenceDto>> getAllVotesByUsername(GetAllVotesByUsernameQuery request, SecurityContext securityContext) {
        log.debug(">>>P" + securityContext.getUserPrincipal().getName());
        de.hsos.swa.application.output.repository.dto.out.RepositoryResult<List<VoteQueryDto>> votesResult = voteRepository.getAllVotesByUser(request.username());

        if (votesResult.error()) {
            return ApplicationResult.exception("Cannot find Posts");
        }

        List<VoteWithVotedEntityReferenceDto> userVotes = votesResult.get().stream().map(v -> new VoteWithVotedEntityReferenceDto(v.vote(), v.votedEntityType(), v.votedEntityId())).toList();

        return ApplicationResult.ok(userVotes);
    }
}
