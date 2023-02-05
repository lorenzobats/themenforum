package de.hsos.swa.application.service.query;

import de.hsos.swa.application.annotations.ApplicationService;
import de.hsos.swa.application.input.GetAllUsersUseCase;
import de.hsos.swa.application.input.dto.out.ApplicationResult;
import de.hsos.swa.application.output.auth.AuthorizationGateway;
import de.hsos.swa.application.output.auth.dto.in.AuthorizationResult;
import de.hsos.swa.application.output.repository.UserRepository;
import de.hsos.swa.application.output.repository.dto.in.RepositoryResult;
import de.hsos.swa.application.service.AuthorizationResultMapper;
import de.hsos.swa.domain.entity.User;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;

@RequestScoped
@Transactional(Transactional.TxType.REQUIRES_NEW)
@ApplicationService
public class GetAllUsersService implements GetAllUsersUseCase {
    @Inject
    UserRepository userRepository;
    @Inject
    AuthorizationGateway authorizationGateway;
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
