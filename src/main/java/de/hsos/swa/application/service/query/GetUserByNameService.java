package de.hsos.swa.application.service.query;

import de.hsos.swa.application.annotations.ApplicationService;
import de.hsos.swa.application.input.dto.in.GetUserByNameQuery;
import de.hsos.swa.application.input.query.GetUserByNameUseCase;
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

/**
 * Die Application Service Klasse GetUserByNameService implementiert das Interface
 * GetUserByNameUseCase der Boundary des Application-Hexagons.
 * Es realisiert die Applikationslogik für das Laden des Users zum übergebenen Namen aus dem User-Repository.
 *
 * @author Lorenzo Battiston
 * @author Oliver Schlüter
 * @version 1.0
 * @see GetUserByNameUseCase                    Korrespondierender Input-Port für diesen Service
 * @see GetUserByNameQuery                      Korrespondierendes Request-DTO für diesen Service
 * @see UserRepository                          Output-Port zum Laden der User
 * @see AuthorizationGateway                    Output-Port zum Prüfen der Leseberechtigung
 */
@RequestScoped
@Transactional(Transactional.TxType.REQUIRES_NEW)
@ApplicationService
public class GetUserByNameService implements GetUserByNameUseCase {

    @Inject
    UserRepository userRepository;

    @Inject
    AuthorizationGateway authorizationGateway;

    /**
     * Lädt einen User aus dem User-Repository zum übergebenen Nutzernamen.
     *
     * @param query             der Name des Nutzers dessen Daten geladen werden sollen
     * @param requestingUser    der anfragende Nutzer, der vom Authorization Gateway berechtigt wird für den Lesezugriff
     * @return ApplicationResult<User> enthält den gesuchten Nutzer zum übergebenen Namen
     */
    @Override
    public ApplicationResult<User> getUserByName(GetUserByNameQuery query, String requestingUser) {
        AuthorizationResult<Boolean> access = authorizationGateway.canAccessUsers(requestingUser);
        if(access.denied())
            return AuthorizationResultMapper.handleRejection(access.status());

        RepositoryResult<User> result = this.userRepository.getUserByName(query.username());
        if (result.error()) {
            if (result.status() == RepositoryResult.Status.ENTITY_NOT_FOUND) {
                return ApplicationResult.notFound("Cannot find user: " + query.username());
            }
            return ApplicationResult.exception();
        }

        return ApplicationResult.ok(result.get());
    }
}
