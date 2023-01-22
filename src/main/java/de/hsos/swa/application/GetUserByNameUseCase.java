package de.hsos.swa.application;

import de.hsos.swa.application.output.Result;
import de.hsos.swa.application.input.dto.in.GetUserByNameInputPortRequest;
import de.hsos.swa.application.input.GetUserByNameInputPort;
import de.hsos.swa.application.output.repository.UserRepository;
import de.hsos.swa.domain.entity.User;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class GetUserByNameUseCase implements GetUserByNameInputPort {

    @Inject
    UserRepository userRepository;

    @Override
    public Result<User> getUserByName(GetUserByNameInputPortRequest request) {
        Result<User> userResult = this.userRepository.getUserByName(request.getUsername());

        if(!userResult.isSuccessful()) {
           return Result.error("Username not found");
        }
        return Result.success(userResult.getData());
    }
}
