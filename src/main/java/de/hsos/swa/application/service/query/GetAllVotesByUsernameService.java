package de.hsos.swa.application.service.query;

import de.hsos.swa.application.annotations.ApplicationService;
import de.hsos.swa.application.input.GetAllVotesByUsernameUseCase;
import de.hsos.swa.application.input.dto.in.GetAllVotesByUsernameQuery;
import de.hsos.swa.application.input.dto.out.ApplicationResult;
import de.hsos.swa.application.input.dto.out.VoteWithVotedEntityReference;
import de.hsos.swa.application.output.auth.AuthorizationGateway;
import de.hsos.swa.application.output.auth.dto.in.AuthorizationResult;
import de.hsos.swa.application.output.repository.dto.in.VoteQueryDto;
import de.hsos.swa.application.output.repository.VoteRepository;
import de.hsos.swa.application.output.repository.dto.out.RepositoryResult;
import de.hsos.swa.application.service.AuthorizationResultMapper;
import org.jboss.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;

@RequestScoped
@Transactional(Transactional.TxType.REQUIRES_NEW)
@ApplicationService
public class GetAllVotesByUsernameService implements GetAllVotesByUsernameUseCase {

    @Inject
    VoteRepository voteRepository;

    @Inject
    AuthorizationGateway authorizationGateway;

    @Override
    public ApplicationResult<List<VoteWithVotedEntityReference>> getAllVotesByUsername(GetAllVotesByUsernameQuery query, String requestingUser) {
        AuthorizationResult<Boolean> access = authorizationGateway.canAccessVotesBy(requestingUser, query.username());
        if(access.denied())
            return AuthorizationResultMapper.handleRejection(access.status());

        RepositoryResult<List<VoteQueryDto>> result = voteRepository.getAllVotesByUser(query.username());
        if (result.error())
            return ApplicationResult.notFound();

        List<VoteWithVotedEntityReference> userVotes = result.get().stream().map(v -> new VoteWithVotedEntityReference(v.vote(), v.votedEntityType(), v.votedEntityId())).toList();
        return ApplicationResult.ok(userVotes);
    }
}
