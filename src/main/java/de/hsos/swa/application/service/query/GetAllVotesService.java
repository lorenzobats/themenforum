package de.hsos.swa.application.service.query;

import de.hsos.swa.application.annotations.ApplicationService;
import de.hsos.swa.application.input.GetAllVotesUseCase;
import de.hsos.swa.application.input.dto.out.ApplicationResult;
import de.hsos.swa.application.input.dto.out.VoteWithVotedEntityReferenceDto;
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
    public ApplicationResult<List<VoteWithVotedEntityReferenceDto>> getAllVotes(SecurityContext securityContext) {
        de.hsos.swa.application.output.repository.dto.out.RepositoryResult<List<VoteQueryDto>> votesResult = voteRepository.getAllVotes();

        if (votesResult.badResult()) {
            return ApplicationResult.error("Cannot find Posts");
        }

        List<VoteWithVotedEntityReferenceDto> userVotes = votesResult.get().stream().map(v -> new VoteWithVotedEntityReferenceDto(v.vote(), v.votedEntityType(), v.votedEntityId())).toList();

        return ApplicationResult.success(userVotes);
    }
}
