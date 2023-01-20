package de.hsos.swa.application.use_case;

import de.hsos.swa.application.port.input.Result;
import de.hsos.swa.application.port.input.request.GetUserByNameInputPortRequest;
import de.hsos.swa.application.port.input.GetUserByNameInputPort;
import de.hsos.swa.application.port.output.UserRepository;
import de.hsos.swa.domain.entity.User;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class GetUserByNameUseCase implements GetUserByNameInputPort {

    @Inject
    UserRepository userRepository;

    @Override
    public Result<User> getUserByName(GetUserByNameInputPortRequest inputPortRequest) {
        Result<User> userResult = this.userRepository.getUserByName(inputPortRequest.getUsername());

        if(!userResult.isSuccessful()) {
           return Result.error("Username not found");
        }
        return Result.success(userResult.getData());
    }
}
