package de.hsos.swa.application.service;

import de.hsos.swa.application.port.input.getUserByName.GetUserByNameInputPortRequest;
import de.hsos.swa.application.port.input.getUserByName.GetUserByNameInputPortResponse;
import de.hsos.swa.application.port.input.getUserByName.GetUserByNameInputPort;
import de.hsos.swa.application.port.output.getUserByName.GetUserByNameOutputPort;
import de.hsos.swa.application.port.output.getUserByName.GetUserByNameOutputPortRequest;
import de.hsos.swa.application.port.output.getUserByName.GetUserByNameOutputPortResponse;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class GetUserByNameUseCase implements GetUserByNameInputPort {

    @Inject
    GetUserByNameOutputPort getUserByNameOutputPort;

    @Override
    public GetUserByNameInputPortResponse getUserByName(GetUserByNameInputPortRequest inputPortRequest) {
        // TODO: Alternative zu Mapping bei reinen CRUD Anfragen?
        GetUserByNameOutputPortRequest getUserByNameOutputPortRequest = new GetUserByNameOutputPortRequest(inputPortRequest.getUsername());
        GetUserByNameOutputPortResponse getUserByNameOutputPortResponse = this.getUserByNameOutputPort.getUserByName(getUserByNameOutputPortRequest);
        GetUserByNameInputPortResponse getUserByNameInputPortResponse = new GetUserByNameInputPortResponse(getUserByNameOutputPortResponse.getId(), getUserByNameOutputPortResponse.getUsername());
        return getUserByNameInputPortResponse;
    }
}
