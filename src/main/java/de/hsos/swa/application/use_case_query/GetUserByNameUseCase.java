package de.hsos.swa.application.use_case_query;

import de.hsos.swa.application.input.dto.in.GetUserByNameInputPortRequest;
import de.hsos.swa.application.input.GetUserByNameInputPort;
import de.hsos.swa.application.output.repository.RepositoryResult;
import de.hsos.swa.application.output.repository.UserRepository;
import de.hsos.swa.application.util.Result;
import de.hsos.swa.domain.entity.User;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

@RequestScoped
@Transactional(Transactional.TxType.REQUIRES_NEW)
public class GetUserByNameUseCase implements GetUserByNameInputPort {

    @Inject
    UserRepository userRepository;

    @Override
    public Result<User> getUserByName(GetUserByNameInputPortRequest request) {
        RepositoryResult<User> userResult = this.userRepository.getUserByName(request.username());

        if(userResult.badResult()) {
           return Result.error("Username not found");
        }

        return Result.success(userResult.get());
    }
}
