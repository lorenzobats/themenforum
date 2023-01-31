package de.hsos.swa.application.use_case_query;

import de.hsos.swa.application.input.GetAllVotesByUsernameInputPort;
import de.hsos.swa.application.input.GetAllVotesInputPort;
import de.hsos.swa.application.input.dto.in.GetAllVotesByUsernameInputPortRequest;
import de.hsos.swa.application.input.dto.out.VoteInputPortDto;
import de.hsos.swa.application.output.dto.VoteOutputPortDto;
import de.hsos.swa.application.output.repository.RepositoryResult;
import de.hsos.swa.application.output.repository.VoteRepository;
import de.hsos.swa.application.util.Result;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.core.SecurityContext;
import java.util.List;

@RequestScoped
@Transactional(Transactional.TxType.REQUIRES_NEW)
public class GetAllVotesUseCase implements GetAllVotesInputPort {

    @Inject
    VoteRepository voteRepository;

    @Override
    public Result<List<VoteInputPortDto>> getAllVotes(SecurityContext securityContext) {
        RepositoryResult<List<VoteOutputPortDto>> votesResult = voteRepository.getAllVotes();

        if (votesResult.badResult()) {
            return Result.error("Cannot find Posts");
        }

        List<VoteInputPortDto> userVotes = votesResult.get().stream().map(v -> new VoteInputPortDto(v.vote(), v.votedEntityType(), v.votedEntityId())).toList();

        return Result.success(userVotes);
    }
}
