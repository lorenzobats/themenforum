package de.hsos.swa.application.service;

import de.hsos.swa.application.port.input._shared.Result;
import de.hsos.swa.application.port.input.getUserByName.GetUserByNameInputPortRequest;
import de.hsos.swa.application.port.input.getUserByName.GetUserByNameInputPortResponse;
import de.hsos.swa.application.port.input.getUserByName.GetUserByNameInputPort;
import de.hsos.swa.application.port.output.user.getUserByName.GetUserByNameOutputPort;
import de.hsos.swa.application.port.output.user.getUserByName.GetUserByNameOutputPortRequest;
import de.hsos.swa.application.port.output.user.getUserByName.GetUserByNameOutputPortResponse;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class GetUserByNameUseCase implements GetUserByNameInputPort {

    @Inject
    GetUserByNameOutputPort getUserByNameOutputPort;

    @Override
    public Result<GetUserByNameInputPortResponse> getUserByName(GetUserByNameInputPortRequest inputPortRequest) {
        // TODO: Alternative zu Mapping bei reinen CRUD Anfragen?
        GetUserByNameOutputPortRequest getUserByNameOutputPortRequest = new GetUserByNameOutputPortRequest(inputPortRequest.getUsername());
        Result<GetUserByNameOutputPortResponse> getUserByNameOutputPortResponse = this.getUserByNameOutputPort.getUserByName(getUserByNameOutputPortRequest);

        if(!getUserByNameOutputPortResponse.isSuccessful()) {
           return Result.error("Username not found");
        }
        return Result.success(new GetUserByNameInputPortResponse(getUserByNameOutputPortResponse.getData().getId(), getUserByNameOutputPortResponse.getData().getUsername()));
    }
}
