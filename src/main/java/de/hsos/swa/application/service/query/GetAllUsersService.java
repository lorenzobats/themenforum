package de.hsos.swa.application.service.query;

import de.hsos.swa.application.annotations.ApplicationService;
import de.hsos.swa.application.input.query.GetAllUsersUseCase;
import de.hsos.swa.application.input.dto.out.ApplicationResult;
import de.hsos.swa.application.output.auth.AuthorizationGateway;
import de.hsos.swa.application.output.auth.dto.in.AuthorizationResult;
import de.hsos.swa.application.output.repository.UserRepository;
import de.hsos.swa.application.output.repository.dto.in.RepositoryResult;
import de.hsos.swa.application.service.util.AuthorizationResultMapper;
import de.hsos.swa.domain.entity.User;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;

/**
 * Die Application Service Klasse GetAllUsersService implementiert das Interface
 * GetAllUsersUseCase der Boundary des Application-Hexagons.
 * Es realisiert die Applikationslogik für das Laden aller User aus dem User-Repository.
 *
 * @author Lorenzo Battiston
 * @author Oliver Schlüter
 * @version 1.0
 * @see GetAllUsersUseCase                 Korrespondierender Input-Port für diesen Service
 * @see UserRepository                     Output-Port zum Laden der User
 * @see AuthorizationGateway               Output-Port zum Prüfen der Leseberechtigung
 */
@RequestScoped
@Transactional(Transactional.TxType.REQUIRES_NEW)
@ApplicationService
public class GetAllUsersService implements GetAllUsersUseCase {
    @Inject
    UserRepository userRepository;
    @Inject
    AuthorizationGateway authorizationGateway;

    /**
     * Lädt alle User aus dem User-Repository.
     *
     * @param requestingUser der anfragende Nutzer, der vom Authorization Gateway berechtigt wird für den Lesezugriff
     * @return ApplicationResult<List<User>> enthält die Liste aller Nutzer des Themenforums
     */
    @Override
    public ApplicationResult<List<User>> getAllUsers(String requestingUser) {
        AuthorizationResult<Boolean> access = authorizationGateway.canAccessUsers(requestingUser);
        if(access.denied())
            return AuthorizationResultMapper.handleRejection(access.status());

        RepositoryResult<List<User>> result = this.userRepository.getAllUsers();
        if(result.error())
            return ApplicationResult.exception();

        return ApplicationResult.ok(result.get());
    }
}
