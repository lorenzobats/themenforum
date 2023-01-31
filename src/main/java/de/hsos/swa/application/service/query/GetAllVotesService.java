package de.hsos.swa.application.service.query;

import de.hsos.swa.application.annotations.ApplicationService;
import de.hsos.swa.application.input.GetAllVotesUseCase;
import de.hsos.swa.application.input.dto.out.Result;
import de.hsos.swa.application.input.dto.out.VoteInputPortDto;
import de.hsos.swa.application.output.repository.dto.in.VoteQueryDto;
import de.hsos.swa.application.output.repository.VoteRepository;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.core.SecurityContext;
import java.util.List;

@RequestScoped
@Transactional(Transactional.TxType.REQUIRES_NEW)
@ApplicationService
public class GetAllVotesService implements GetAllVotesUseCase {

    @Inject
    VoteRepository voteRepository;

    @Override
    public Result<List<VoteInputPortDto>> getAllVotes(SecurityContext securityContext) {
        de.hsos.swa.application.output.repository.dto.out.RepositoryResult<List<VoteQueryDto>> votesResult = voteRepository.getAllVotes();

        if (votesResult.badResult()) {
            return Result.error("Cannot find Posts");
        }

        List<VoteInputPortDto> userVotes = votesResult.get().stream().map(v -> new VoteInputPortDto(v.vote(), v.votedEntityType(), v.votedEntityId())).toList();

        return Result.success(userVotes);
    }
}
