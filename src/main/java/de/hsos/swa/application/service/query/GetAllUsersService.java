package de.hsos.swa.application.service.query;

import de.hsos.swa.application.annotations.ApplicationService;
import de.hsos.swa.application.input.GetAllUsersUseCase;
import de.hsos.swa.application.input.dto.out.ApplicationResult;
import de.hsos.swa.application.output.repository.UserRepository;
import de.hsos.swa.application.output.repository.dto.out.RepositoryResult;
import de.hsos.swa.domain.entity.User;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.core.SecurityContext;
import java.util.List;
// TODO implementieren Users
@RequestScoped
@Transactional(Transactional.TxType.REQUIRES_NEW)
@ApplicationService
public class GetAllUsersService implements GetAllUsersUseCase {
    @Inject
    UserRepository userRepository;
    @Override
    public ApplicationResult<List<User>> getAllUsers(SecurityContext securityContext) {
        // TODO: Security Context über auth service
        RepositoryResult<List<User>> userResult = this.userRepository.getAllUsers();
        if(userResult.badResult()){
            return ApplicationResult.error("Didnt find users");
        }
        return ApplicationResult.success(userResult.get());
    }
}
