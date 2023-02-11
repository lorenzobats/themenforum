package de.hsos.swa.application.service.query;

import de.hsos.swa.application.annotations.ApplicationService;
import de.hsos.swa.application.input.query.GetAllVotesByUsernameUseCase;
import de.hsos.swa.application.input.dto.in.GetAllVotesByUsernameQuery;
import de.hsos.swa.application.input.dto.out.ApplicationResult;
import de.hsos.swa.application.input.dto.out.VoteWithVotedEntityReference;
import de.hsos.swa.application.output.auth.AuthorizationGateway;
import de.hsos.swa.application.output.auth.dto.in.AuthorizationResult;
import de.hsos.swa.application.output.repository.dto.in.VoteQueryDto;
import de.hsos.swa.application.output.repository.VoteRepository;
import de.hsos.swa.application.output.repository.dto.in.RepositoryResult;
import de.hsos.swa.application.service.util.AuthorizationResultMapper;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;

/**
 * Die Application Service Klasse GetAllVotesByUsernameService implementiert das Interface
 * GetAllVotesByUsernameUseCase der Boundary des Application-Hexagons.
 * Es realisiert die Applikationslogik für das Laden aller Votes eines Users aus dem Vote-Repository.
 *
 * @author Lorenzo Battiston
 * @author Oliver Schlüter
 * @version 1.0
 * @see GetAllVotesByUsernameUseCase        Korrespondierender Input-Port für diesen Service
 * @see GetAllVotesByUsernameQuery          Korrespondierendes Request-DTO für diesen Service
 * @see VoteRepository                      Output-Port zum Laden der Votes
 * @see AuthorizationGateway                Output-Port zum Prüfen der Leseberechtigung
 */
@RequestScoped
@Transactional(Transactional.TxType.REQUIRES_NEW)
@ApplicationService
public class GetAllVotesByUsernameService implements GetAllVotesByUsernameUseCase {

    @Inject
    VoteRepository voteRepository;

    @Inject
    AuthorizationGateway authorizationGateway;

    /**
     * Lädt alle Votes eines Users aus dem Vote-Repository.
     *
     * @param query enthält den Namen des Vote-Inhabers, dessen Votes geladen werden sollen
     * @param requestingUser der anfragende Nutzer, der vom Authorization Gateway berechtigt wird für den Lesezugriff
     * @return ApplicationResult<List<VoteWithVotedEntityReference>> enthält die Liste aller Votes des Themenforums,
     * die zu dem Nutzer gehören, inklusive der ID und des Typs der referenzierten Entität.
     */
    @Override
    public ApplicationResult<List<VoteWithVotedEntityReference>> getAllVotesByUsername(GetAllVotesByUsernameQuery query, String requestingUser) {
        AuthorizationResult<Boolean> access = authorizationGateway.canAccessVotesBy(requestingUser, query.username());
        if(access.denied())
            return AuthorizationResultMapper.handleRejection(access.status());

        RepositoryResult<List<VoteQueryDto>> result = voteRepository.getAllVotesByUser(query.username());
        if (result.error())
            return ApplicationResult.notFound();

        List<VoteWithVotedEntityReference> userVotes = result.get().stream()
                .map(v -> new VoteWithVotedEntityReference(v.vote(), v.votedEntityType(), v.votedEntityId())).toList();
        return ApplicationResult.ok(userVotes);
    }
}
