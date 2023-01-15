package de.hsos.swa.application.port.output.getUserByName;


import de.hsos.swa.application.port.input._shared.Result;

public interface GetUserByNameOutputPort {
    Result<GetUserByNameOutputPortResponse> getUserByName(GetUserByNameOutputPortRequest outputPortRequest);
}
