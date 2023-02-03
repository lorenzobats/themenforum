package de.hsos.swa.application.service.query;

import de.hsos.swa.application.annotations.ApplicationService;
import de.hsos.swa.application.input.dto.in.GetUserByNameQuery;
import de.hsos.swa.application.input.GetUserByNameUseCase;
import de.hsos.swa.application.input.dto.out.ApplicationResult;
import de.hsos.swa.application.output.auth.AuthorizationGateway;
import de.hsos.swa.application.output.auth.dto.in.AuthorizationResult;
import de.hsos.swa.application.output.repository.UserRepository;
import de.hsos.swa.application.output.repository.dto.out.RepositoryResult;
import de.hsos.swa.application.service.AuthorizationResultMapper;
import de.hsos.swa.domain.entity.User;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

@RequestScoped
@Transactional(Transactional.TxType.REQUIRES_NEW)
@ApplicationService
public class GetUserByNameService implements GetUserByNameUseCase {

    @Inject
    UserRepository userRepository;

    @Inject
    AuthorizationGateway authorizationGateway;
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
