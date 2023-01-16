package de.hsos.swa.application.use_case;

import de.hsos.swa.application.port.input._shared.Result;
import de.hsos.swa.application.port.input.getUserByName.GetUserByNameInputPortRequest;
import de.hsos.swa.application.port.input.getUserByName.GetUserByNameInputPortResponse;
import de.hsos.swa.application.port.input.getUserByName.GetUserByNameInputPort;
import de.hsos.swa.application.port.output.user.GetUserByNameOutputPort;
import de.hsos.swa.domain.entity.User;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class GetUserByNameUseCase implements GetUserByNameInputPort {

    @Inject
    GetUserByNameOutputPort getUserByNameOutputPort;

    @Override
    public Result<GetUserByNameInputPortResponse> getUserByName(GetUserByNameInputPortRequest inputPortRequest) {
        Result<User> getUserByNameOutputPortResponse = this.getUserByNameOutputPort.getUserByName(inputPortRequest.getUsername());
        if(!getUserByNameOutputPortResponse.isSuccessful()) {
           return Result.error("Username not found");
        }
        // TODO: DTO zurückgeben
        return Result.success(new GetUserByNameInputPortResponse(getUserByNameOutputPortResponse.getData().getId(), getUserByNameOutputPortResponse.getData().getName()));
    }
}
