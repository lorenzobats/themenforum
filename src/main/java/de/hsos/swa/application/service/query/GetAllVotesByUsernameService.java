package de.hsos.swa.application.service.query;

import de.hsos.swa.application.annotations.ApplicationService;
import de.hsos.swa.application.input.GetAllVotesByUsernameUseCase;
import de.hsos.swa.application.input.dto.in.GetAllVotesByUsernameQuery;
import de.hsos.swa.application.input.dto.out.VoteInputPortDto;
import de.hsos.swa.application.output.repository.dto.in.VoteQueryDto;
import de.hsos.swa.application.output.repository.dto.out.RepositoryResult;
import de.hsos.swa.application.output.repository.VoteRepository;
import de.hsos.swa.application.input.dto.out.Result;
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
    public Result<List<VoteInputPortDto>> getAllVotesByUsername(GetAllVotesByUsernameQuery request, SecurityContext securityContext) {
        log.debug(">>>P" + securityContext.getUserPrincipal().getName());
        RepositoryResult<List<VoteQueryDto>> votesResult = voteRepository.getAllVotesByUser(request.username());

        if (votesResult.badResult()) {
            return Result.error("Cannot find Posts");
        }

        List<VoteInputPortDto> userVotes = votesResult.get().stream().map(v -> new VoteInputPortDto(v.vote(), v.votedEntityType(), v.votedEntityId())).toList();

        return Result.success(userVotes);
    }
}
