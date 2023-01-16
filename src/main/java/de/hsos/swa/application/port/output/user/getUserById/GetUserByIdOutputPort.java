package de.hsos.swa.application.port.output.user.getUserById;


import de.hsos.swa.application.port.input._shared.Result;

public interface GetUserByIdOutputPort {
    Result<GetUserByIdOutputPortResponse> getUserById(GetUserByIdOutputPortRequest outputPortRequest);
}
