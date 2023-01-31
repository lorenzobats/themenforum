package de.hsos.swa.application.service.query;

import de.hsos.swa.application.annotations.ApplicationService;
import de.hsos.swa.application.input.dto.in.GetUserByNameQuery;
import de.hsos.swa.application.input.GetUserByNameUseCase;
import de.hsos.swa.application.input.dto.out.Result;
import de.hsos.swa.application.output.repository.UserRepository;
import de.hsos.swa.application.output.repository.dto.out.RepositoryResult;
import de.hsos.swa.domain.entity.User;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.core.SecurityContext;

@RequestScoped
@Transactional(Transactional.TxType.REQUIRES_NEW)
@ApplicationService
public class GetUserByNameService implements GetUserByNameUseCase {

    @Inject
    UserRepository userRepository;

    @Override
    public Result<User> getUserByName(GetUserByNameQuery request, SecurityContext securityContext) {
        // TODO: Security Context ( Nur Admin !)
        RepositoryResult<User> userResult = this.userRepository.getUserByName(request.username());

        if(userResult.badResult()) {
           return Result.error("Username not found");
        }

        return Result.success(userResult.get());
    }
}
